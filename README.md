# PaulaE-BankSystem

Welcome to the readme of the Baking Systems made by Paula Esteban Barrigón. 
In this documment you can find all the information needed to understand my understanding of the task.

## 1.	Description of the project

This proyect represented a banking system of the which I will give a short explanation in this section.

Firstly, the banking system has 4 different kinds of accounts (which are explain in detail in the "model" section): 
  - Checking account
  - Saving account
  - Credit Card account
  - Student checking account 

Aditionally, the banking systems has 3 different types of users (which are explain in detail in the "model" section): 
  - Admin (person responsable of the well funtionning of the system)
  - Account holders (any person who is primary or secondary owner or both of any of the previosly named bank accounts types)
  - Thrid Party (external entity of the bank that can send and recieved money)

By using SpringBoot Security, we regulate the access of the different types of users to the different posibles funtionalities of the systems (we will go in more detail
about all the this in the "server route tables" section) but, as a brief sumary:

  1. Creation of admin --> everyone is allow. In reality, only admin should be allow to because admins should be defaulty defined in the system, but since I work with empty sql tables 
at the beggining, that general authorization was needed to use the system.

  2. Removal of admin --> everyone is allow. Same reasoning as the previous one.

  3. Creation of a bank Account  --> only admins ae allow.
     >Important notice : When created a bank account, the accounth holder (Primary and/or Secondary owner) are created at the same time if they don´t existed in the datebase. 
                        Creation of an account holder alone is not possible
            
  4. Removal of a bank Account --> only admins are allow.
    > Important notice: When remove a bank account, the account holder (Primery and/or Secondary owner) are removed at the same time if they dont have any other account in the system.
                       Removal of account holder alone is not possible.
                       
  5. Creation of thrid parties --> only admins are allow.
  
  6. Removal of thrid parties --> only admins are allow.
  
  7. Check balance of an account --> admins and the account holder of the account (both primary and secondary owner) are allow.
     > Important notice: when the balance of an account is checked, it is also checked if the interest rate or maintenance fee should be apply and, in case it is, 
                      it is apply before the visualizing of the balance.
               
  8. Modify account balance --> only admins 
  
  9. Tranfer fund --> There are two 3 types of transacction 
        1. Transfer from and account to another account --> only the primary or secondary owner of the account origen is allow.
        2. Tranfer from an account to a thrid party --> only the primary or secondary owner of the account origen is allow.
        3. Tranfer from a thridparty to an account --> only the thrid party in cuestion is allow.

      >Important notice: every time a transaction is made it is check if it could be a fraud case. A fraud case is consider if an account send 2 transaction in under 30s.
                      In the requirements, it was said that is should be under 1s but, in order to verify the correct working of my system, I modify the time limit.
                      When a Fraud case is discover, the second transaction is denied and the status of the account is change to FREEZE. 
                      Lastly, frraud is only trackable in account with status, which means, all of them except from Credit Cards.

## 2.	Technologies Used

This proyect was develop in Java and Spring-Boot.
For the databases we use MySQL.
The proyect was launched using our local server so, PostMan was choose as the tool for doing the request and verify the correct functioning of the system.
And for the security we used Spring-Boot-Security.

## 3.	Models

Firstly, here you can see a picture of the Class diagram:

![BankSystem](https://user-images.githubusercontent.com/90968486/169654993-fc3f46b9-be47-4d0c-9372-f2dadbd8021c.png)

As wee can see, the different types of account extends from the Class "Account".

The different accounts have different properties, always following the instructions. However, some freendom where taking in the application of some methods. 
  Example: the "Student Checking" even tho has the attribute "penalty fee" does not have a method that will apply it. The reason behind it is that with the situations 
  explaineded in the requeriments (penalty fee is applied when the balance < miminum balance), it will never bee fullfil in that type of account. 

Users following the same squema as Accounts. All the users exteds the Class "User".

## 4.	Server routes table(Method, Route or URL, Description as columns)

In this section, we are going to explain the different end points as well as explain in more detail the methods which I believed either by it functionality or its
caractheristics deserve a special attention.

#### Create new checking accounts:

  ```http
  POST /api/checkings
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `CheckingDTO` | `CheckingDTO` | **Required**. **Valid** Account to be save |

    CheckingDTO --> has as mandatory attributes to provide: Money Balance, AccountHolderDTO primary Owner, String secretKey
                    has as optional attributes to provide: AccountHolderDTO secondary Owner.
                    
    AccountHolder DTO --> has as mandatory attributes to provide: String username, String password, String dateOfBirth, Address primaryAddres.
                          has as optional attributes to provide: String mailAddress
                          
   Important notice: if the primary Owner age  is < 24. The account is automatically create as StudentChecking.

#### Create new savings accounts:

  ```http
  POST /api/savings
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `SavingsDTO` | `SavingsDTO` | **Required**. **Valid** Account to be save |

    SavingsDTO -->  has as mandatory attributes to provide: Money Balance, AccountHolderDTO primary Owner, String secretKey
                    has as optional attributes to provide: AccountHolderDTO secondary Owner, BigDecimal Interest Rate
            
#### Create new credit cards accounts:

  ```http
  POST /api/creditcards
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `creditCardDTO` | `CreditCardaDTO` | **Required**. **Valid** Account to be save |

    CreditCardDTO -->  has as mandatory attributes to provide: Money Balance, AccountHolderDTO primary Owner
                       has as optional attributes to provide: AccountHolderDTO secondary Owner, BigDecimal Interest Rate, Money creditLimit
                       

#### Create admin users:

  ```http
  POST /api/admins
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `adminDTO` | `adminDTO` | **Required**. **Valid** user to be save |

    adminDTO -->  has as mandatory attributes to provide: String name, String username, String password
  
#### Create thridparty users:
                       
  ```http
  POST /api/thirdparties
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `thridPartyDTO` | `thridPartyDTO` | **Required**. **Valid** user to be save |

    thridPartyDTO -->  has as mandatory attributes to provide: String name, String username, String password, String hasedKey


#### Delete account and users:

  ```http
  DELETE /api/checkings/{id}
```
  ```http
  DELETE /api/savings/{id}
```
  ```http
  DELETE /api/creditcards/{id}
```
  ```http
  DELETE /api/studentscheckings/{id}
```
  ```http
  DELETE /api/admins/{id}
```
  ```http
  DELETE /api/thridsparties/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `int` | **Required**. The id of the account or user to delete |


#### Get the balance of an account

   ```http
  GET /api/checkings/{id}
```
  ```http
  GET /api/savings/{id}
```
  ```http
  GET /api/creditcards/{id}
```
  ```http
  GET /api/studentcheckingss/{id}
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `int` | **Required**. The id of the account to check the balance of |

  Important notice: When balance is checked, the systems verify is any interest rate / commision fee should be apply.
  
                    1. Penalty Fee --> in Checkings and Savings if balance < minimum balance
                    
                    2. Monthly Maintenance Fee --> in Checkings if more of 30 days have past since it was apply for the last time
                    
                    3. Interest rate --> in Credit Cardid if more than 30 days have past since it was apply for the last time.
                    
                                         In Savings if more than 365 days have past since it was apply for the last time.
                                         

### Update de balance of an account
  ```http
  PATCH /api/checkings/{id}
```
  ```http
  PATCH /api/savings/{id}
```
  ```http
  PATCH /api/studentcheckings/{id}
```
  ```http
  PATCH /api/creditcards/{id}
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `int` | **Required**. The id of the account to update the balance of |
| `balanceDTO` | `balanceDTO` | **Required**. **Valid** Money to update the balance |
    
    BalanceDTO --> has as mandatory attributes to provide: Money Balance
    

### Transfer fund

As we have mentioned previosly, there are 3 different possible transactions: 
    Account --> Account 
    ThridParty --> Account
    Account --> ThridParty
    
When an account is the one ordering the trasferation of funds the requirements said that the accountHolder should provide (in reference to their own account): 
      1. The account holder log in must be the secondary or primary owner of the account 
      2. The account holder must provide the name of the secondary / primary owner of the account 
          Aclaration: if it is the primary owner the one log in, they must provide the primary owner name
      3. The amunth of money they want to send. 

So, in order to choose the account from the account holder (they can have many different accounts in the system) from which the one the money is going to be send from,
we develop the following algorithm. The reason of doing so is that the account holder doesnt provide an specific "id" so, in reality many different account could
match with the information the account holder is provide.
      
Firtly, we look in the system for all the account (independently of the type) where the log user is either the primary or the secondary owner.
Secondly, we look if in any of those accounts the primary or secondary owner is the one provide by the user and if the balance > amounth to sent
      
  Until here everything is done with queries: 
  Example in the checking accounts: 
    @Query(nativeQuery = true, value = "SELECT * FROM checking JOIN account_holder ON checking.primary_owner_id = account_holder.user_id " +
            "OR checking.secondary_owner_id = account_holder.user_id WHERE account_holder.username = :name AND account_holder.name = :username AND checking.balance > :amounth ")
    List<Checking> findBalanceByUserNameAndNameAndAmount(String name, String username, BigDecimal amounth);
  
 After having all the possible accounts from which the transaction could be made, we look for the one that has the highst balance which will be selected as the one 
 to done the transaction from. We select the higest balance as the final selection requirement because since the penalty fee is apply if the balance < minimum balance,
 that criteria minimaze the chance of the users of the banking system to being paying a penalty fee.

 Finally, before processing the transaction, it is checked if a fraud could be happing. If not, the transaction continous. If yes, the transaction is cancell and 
 the account is free.
 
 Now, after this introduction, let see the api reference of the different types of funds transferation
 
 1. Account --> Account
  ```http
  PATCH /api/accountholder/transferfunds
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `String` | **Required**.**Valid** Name of the primary or secondary owner |
| `id` | `int` | **Required**. **Valid** Account´s ID where the money is going to |
| `quantity` | `Big Decimal` | **Required**. Amouth of money to tranfer |
  
 1. Account --> ThridParty
  ```http
  PATCH /api/accountholder/transferfunds/thridparty
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `String` | **Required**. **Valid** Name of the primary or secondary owner |
| `TPname` | `String` | **Required**. **Valid** Name of the thrid party the money is going to |
| `quantity` | `Big Decimal` | **Required**. Amouth of money to tranfer |

  
 1.ThridParty --> Account 
  ```http
  PATCH /api/thridparty/transferfund
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `hashKey` | `String` | **Required**. **Valid** Hash key of the thrid party ordering the transferation |
| `secretKey` | `String` | **Required**. **Valid** Secret key of the account the money is going to |
| `id` | `int` | **Required**. **Valid** Account´s ID where the money is going to |
| `amouth` | `Big Decimal` | **Required**. Amouth of money to tranfer |
  
  
## 5.	Resources
In order to complete this proyect Google, Shaun and Raymond were use are sources. Thank you all for sharing your knowledge with me!


