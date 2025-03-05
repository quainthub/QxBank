package com.quaint.qx_bank.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.quaint.qx_bank.dto.BankStatementRequest;
import com.quaint.qx_bank.dto.EmailDetails;
import com.quaint.qx_bank.entity.Transaction;
import com.quaint.qx_bank.entity.User;
import com.quaint.qx_bank.repository.TransactionRepository;
import com.quaint.qx_bank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BankStatementServiceImpl implements BankStatementService {
    /**
     * retrieve list of transactions within a state range given an account number
     * generate a pdf file of transactions
     * send the file via email
     */

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private static final String FILE = ".\\files\\SampleStatement.pdf";

    @Override
    public List<Transaction> generateBankStatement(BankStatementRequest bankStatementRequest) throws DocumentException, FileNotFoundException {
        boolean isAccountExist = userRepository.existsByAccountNumber(bankStatementRequest.getAccountNumber());
        if (!isAccountExist){
            return new ArrayList<Transaction>();
        }
        List<Transaction> transactionList = transactionRepository.findAll()
                .stream()
                .filter(t ->t.getAccountNumber().equals(bankStatementRequest.getAccountNumber()))
                .filter(t -> t.getCreatedAt().isAfter(bankStatementRequest.getStartDate().atStartOfDay()) &&
                        t.getCreatedAt().isBefore(bankStatementRequest.getEndDate().plusDays(1).atStartOfDay()))
                .toList();
        User user = userRepository.findByAccountNumber(bankStatementRequest.getAccountNumber());
        designStatement(bankStatementRequest.getStartDate().toString(),
                bankStatementRequest.getEndDate().toString(),
                user,
                transactionList);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("ACCOUNT STATEMENT")
                .messageBody("Kindly find your requested account statement attached!\n" +
                        "Your account details:\n" +
                        "Account Name: "+user.getAccountName()+"\n" +
                        "Account Number: "+user.getAccountNumber()+"\n")
                .attachment(FILE)
                .build();
        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;
    }
    private void designStatement(String startDate,
                                 String endDate,
                                 User user,
                                 List<Transaction> transactionList) throws FileNotFoundException, DocumentException {
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("QX BANK STATEMENT"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("123 ABC Str, NYC NY, 10000"));
        bankAddress.setBorder(0);
        bankAddress.setBackgroundColor(BaseColor.BLUE);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell start = new PdfPCell(new Phrase("Start Date: "+startDate));
        start.setBorder(0);
        PdfPCell end = new PdfPCell(new Phrase("End Date: "+endDate));
        end.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("Statement Type: Account Statement"));
        statement.setBorder(0);
        PdfPCell name = new PdfPCell(new Phrase("Customer Name: "+ user.getAccountName()));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Customer Address: "+ user.getAddress()));
        address.setBorder(0);
        statementInfo.addCell(name);
        statementInfo.addCell(start);
        statementInfo.addCell(statement);
        statementInfo.addCell(end);
        statementInfo.addCell(address);
        statementInfo.addCell(space);

        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("Date"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("Transaction Type"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("Transaction Amount"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("Status"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);
        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(status);

        transactionList.forEach(t -> {
            transactionsTable.addCell(new Phrase(t.getCreatedAt().toString()));
            transactionsTable.addCell(new Phrase(t.getTransactionType()));
            transactionsTable.addCell(new Phrase(t.getAmount().toString()));
            transactionsTable.addCell(new Phrase(t.getStatus()));
        });

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionsTable);

        document.close();
    }
}
