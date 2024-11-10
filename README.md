# Configuration
1. mysql
2. redis
3. rabbitmq
4. elasticsearch(optional)
5. hdfs(optional)

# Fuctionalities

## Email and User
1. email verification code 
2. use google.code.kaptcha to generate image verification codes and add noise
3. register user by email
4. change password (encrypted storage)
5. log in and out (using Shiro)
6. change admin password
7. view user list
8. user IP regional distribution
9. user profile 
10. view online user list
11. modify user ban
12. modify user role
13. remove online users
14. verify email
15. upload avatar 
16. routing permission 

## Role, Menu and Operation log
1. viewing roles 
2. adding and modifying roles
3. deleting roles (roles with users are not allowed to be deleted)
4. role permissions
5. view menu
6. add/modify menu 
7. delete menu 
8. user menu 
9. view operation 
10. adding, deleting and modifying operation logs

## User Homepage 
1. displaying user profile 
2. Creator Center, displays the total number of articles, the total number of public and reviewed articles, the number of followers, the number of comments, the number of likes, the number of views, etc.
3. import user by table(imported users must complete email verification first) 
4. follow, unfollow, followers
5. the content that users want to watch, are watching, and have watched

## Talks, Comments, Articles
1. talks addition, deletion (hard and soft), modification and query (id, full site search, display of discussion comment count, number of likes, whether the current user has liked, image list)
2. like, like cancellation 
3. multi-image upload
4. comments addition (automatic filtering of sensitive words, the word list comes from GitHub), deletion (hard and soft) query (number of likes, number of replies, whether the comment is liked), review (email reminders will be sent if the review fails), likes, and comment tree 
5. comment email notifications
6. sensitive word filtering for articles and talks 
7. article addition, deletion, modification and query 
8. addition and deletion of categories, tags of articles(categories with articles are not allowed to be deleted), modification (top), query (id, tag, category, full site search, likes, views (a user can only be counted once) 
9. previous article, next article, recommended articles (just find the same tag based on the current article tagId)), likes, comments, hot articles (top five views), articles of 10. 10. followers (reverse order), and administrators upload articles to HDFS in md format.
11. article review, add and modify articles to automatically change the status to "under review" and send a message to the administrator to remind that there are new articles that need to be reviewed. After the administrator reviews, he can directly delete illegal articles and change the status to "passed/failed" 
12. draft box
13. article uses MD5 rich text format, filter the content by keywords, distinguish malicious injection words from rich text format prompt words, and convert them for storage and display.

## HDFS, picture and album, document and collection
1. acquisition of HDFS file streams 
2. upload pictures, documents, and videos related to different sections to different folders of HDFS 
3. add and delete(hard, soft, and database levels)  When deleting an album, the pictures are deleted synchronously. When restoring pictures, the album can be restored. 
4. view the album, pictures, add, and move pictures, document for video training section
5. want to watch, watching, and watched 
6. Cancel the want to watch if you are watching, and cancel the content that users want to watch, are watching, and have watched

## Website
1. statistics of web page visits, users, articles, comments, articles, users this week, articles, categories, and tags
2. website configuration update(including website avatar, name, author, introduction, remarks, creation time, filing number, GitHub, Gitee, whether to review comments, whether to notify emails, default article cover, default avatar for new users, etc. (stored in Redis at the same time). 
3. addition, deletion, modification, and query of interfaces
4. the interfaces allowed to be accessed for the corresponding roles 

## Messaging and Notification
sending private messages between users
pin the user chat box to the top, read and unread, and deleted the session 
notifications for likes and comments, system notifications, read and unread 
administrators can modify the notification content to unread 
group chat(sending messages (after sending broadcast messages), withdrawing messages, and sending voice) Voice is saved in HDFS. 
Add a heartbeat message mechanism to ensure chat, and obtained the number of online people based on the session.
received comments, likes notifications, and being followed notifications
display of unread likes, follows, and comments

## Reward Points
1. viewing, due to what event how many points were obtained (consumed), current total number of points 
2. upload of articles to obtain points 
3. points mall
4. add, delete, modify and check user addresses, only the most recent five user addresses are displayed
5. add, delete, modify and check products and product models, and sort by sales volume and amount
6. shopping cart, add items to the shopping cart directly to add the quantity, add item models that are not in the shopping cart, and control the increase and decrease of the number of product models in the shopping cart. Adding is not allowed if the inventory exceeds the inventory, and the inventory is insufficient and it is displayed as sold out
7. orders, allow orders to be generated from the shopping cart or the product itself, counted into sales, and delete the shopping cart content accordingly. Product sales are the sales of all models. Insufficient inventory implements point deduction and transaction rollback, and returns and exchanges realize point compensation
8. freights, allow multiple freights for a single order, and automatically receive the goods within seven days after all freights are sent out
9. orders are classified as closed, completed, and in progress. Undelivered orders are allowed to be cancelled, and order closure and point refunds are realized. Undelivered orders are allowed to change addresses and update delivery status

## Tests and Questionnaires
1. single upload of questions, answers (notes, analysis), labels
2. batch upload of questions, labels and answers (notes, answer analysis) in excel sheets 
3. types can be chosen among single choice, multiple choice, fill in the blank, text, drop-down, cascade, upload, and score question types, and adapt to both practice and questionnaire modes. 
4. cascade questions, allow options to nest questions to display
5. options are in html format
6. delete, view, search, and update questions
7. add, delete, modify, and view templates
8. add, delete, modify, and check projects(test/questionnaire) 
9. unified configuration of projects (whether it is open, whether a password is required, whether the question number is displayed, whether the progress bar is displayed, whether it is automatically saved, whether it is allowed to change the answer (i.e. redo), whether the answer sheet is visible, whether copying questions is allowed, whether the answer and analysis are displayed, start time, end time, if the questionnaire (test paper) is randomly generated, the number of random questions, question types, and which labels can be returned if the randomness is not enough, and if it is enough, the questions are randomly generated. 
10. get the test paper and post the answer 
11. if it is a questionnaire, add reward points 
12. return the percentage of a certain item, the correct answer, whether it is correct, the answer analysis, etc. 
13. view your answers, whether it is correct, the answer, what proportion of people has chosen, etc.
14. administrators analyze the test paper, display the correct rate, and check each question. Fill-in-the-blank questions display the proportion of filled text, and multiple-choice questions display the proportion of options.

## API Documents
1. use knife4j and swagger to automatically export all interface API documents

## Customer Service
1. write some FAQs, allow adding, deleting, disabling questions, modifying similar questions, and displaying FAQs 
2. use Hanlp algorithm to extract keywords, count word frequency, calculate model parameters, inverse document frequency, calculate similarity between questions, use 0.19 as the boundary, distinguish similar questions from non-similar questions, complete automatic answers to questions, or answer "cannot answer".

## Courses
1. course purchase, the user is bound to the document after the points are deducted 
2. recent reading, record the different documents or videos read in the last five times
3. count the number of views of the course, sort the courses by popularity according to the number of views
4. popular courses
5. recommend courses according to course tags
6. course comment
7. add, delete, modify and check live broadcasts
8. classify by live broadcast, preview, and end of live broadcast
9. use video breakpoints to complete progress bar pull and fast forward

## Safety
1. supports both HTTP and HTTPS, configures HTTP connector, redirects HTTP to HTTPS
2. XSS cross-site scripting attack defense, modifies X-Frame-Options so that the address of the frame page can only be a page under the same domain name, preventing it from being embedded in other pages to prevent click hijacking
3. Response header, SQL injection filtering
