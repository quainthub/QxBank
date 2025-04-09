# A Spring Boot REST Application that allows users to perform basic banking functions. 

## API includes the following endpoints:
- Create user account (/api/user)
- Login user account (/api/user/login)
- Balance inquiry (/api/user/balanceInquiry)
- Name inquiry (/api/user/nameInquiry)
- Credit request (/api/user/credit)
- Debit request (/api/user/debit)
- Transfer request (/api/user/transfer)
- Bank Statement (api/transaction/bankStatement)

## Internal utitlities include:
- Email notification based on changes/transactions in an account
- Transaction logging for all operations for an account

## Other Info:
- A database is implemented in MySQL through JPA
- Security layer uses JWT to authenticate user and maintains login tokens after users have logged in
- Documentation is procured through Swagger
- Pdf bank statemnets are generated through IText and sent through email

For any questions or suggestions, please reach out at quaint_xu@yahoo.com.
