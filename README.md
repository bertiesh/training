## Email and User
email verification code 
use google.code.kaptcha to generate image verification codes and add noise
register user by email
change password (encrypted storage)
log in and out (using Shiro)
change admin password
view user list
user IP regional distribution
user profile 
view online user list
modify user ban
modify user role
remove online users
verify email
upload avatar 
routing permission 

## Role, Menu and Operation log
viewing roles 
adding and modifying roles
deleting roles (roles with users are not allowed to be deleted)
role permissions
view menu
add/modify menu 
delete menu 
user menu 
view operation 
adding, deleting and modifying operation logs

## User Homepage 
displaying user profile 
Creator Center, displays the total number of articles, the total number of public and reviewed articles, the number of followers, the number of comments, the number of likes, the number of views, etc.
import user by table(imported users must complete email verification first) 
follow, unfollow, followers
the content that users want to watch, are watching, and have watched

## Talks, Comments, Articles
talks addition, deletion (hard and soft), modification and query (id, full site search, display of discussion comment count, number of likes, whether the current user has liked, image list)
like, like cancellation 
multi-image upload
comments addition (automatic filtering of sensitive words, the word list comes from GitHub), deletion (hard and soft) query (number of likes, number of replies, whether the comment is liked), review (email reminders will be sent if the review fails), likes, and comment tree 
comment email notifications
sensitive word filtering for articles and talks 
article addition, deletion, modification and query 
addition and deletion of categories, tags of articles(categories with articles are not allowed to be deleted), modification (top), query (id, tag, category, full site search, likes, views (a user can only be counted once) 
previous article, next article, recommended articles (just find the same tag based on the current article tagId)), likes, comments, hot articles (top five views), articles of followers (reverse order), and administrators upload articles to HDFS in md format.
Article review, add and modify articles to automatically change the status to "under review" and send a message to the administrator to remind that there are new articles that need to be reviewed. After the administrator reviews, he can directly delete illegal articles and change the status to "passed/failed" 
draft box
article uses MD5 rich text format, filter the content by keywords, distinguish malicious injection words from rich text format prompt words, and convert them for storage and display.

## HDFS, picture and album, document and collection
acquisition of HDFS file streams 
upload pictures, documents, and videos related to different sections to different folders of HDFS 
add and delete(hard, soft, and database levels)  When deleting an album, the pictures are deleted synchronously. When restoring pictures, the album can be restored. 
view the album, pictures, add, and move pictures, document for video training section
want to watch, watching, and watched 
Cancel the want to watch if you are watching, and cancel the content that users want to watch, are watching, and have watched

## Website
statistics of web page visits, users, articles, comments, articles, users this week, articles, categories, and tags
website configuration update(including website avatar, name, author, introduction, remarks, creation time, filing number, GitHub, Gitee, whether to review comments, whether to notify emails, default article cover, default avatar for new users, etc. (stored in Redis at the same time). 
addition, deletion, modification, and query of interfaces
the interfaces allowed to be accessed for the corresponding roles 

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
viewing
due to what event how many points were obtained (consumed)  
current total number of points 
upload of articles to obtain points 
points mall
add, delete, modify and check user addresses, only the most recent five user addresses are displayed
add, delete, modify and check products and product models, and sort by sales volume and amount
ahopping cart, add items to the shopping cart directly to add the quantity, add item models that are not in the shopping cart, and control the increase and decrease of the number of product models in the shopping cart. Adding is not allowed if the inventory exceeds the inventory, and the inventory is insufficient and it is displayed as sold out
orders, allow orders to be generated from the shopping cart or the product itself, counted into sales, and delete the shopping cart content accordingly. Product sales are the sales of all models. Insufficient inventory implements point deduction and transaction rollback, and returns and exchanges realize point compensation
freights, allow multiple freights for a single order, and automatically receive the goods within seven days after all freights are sent out
orders are classified as closed, completed, and in progress. Undelivered orders are allowed to be cancelled, and order closure and point refunds are realized. Undelivered orders are allowed to change addresses and update delivery status

## Tests and Questionnaires
single upload of questions, answers (notes, analysis), labels
batch upload of questions, labels and answers (notes, answer analysis) in excel sheets 
types can be chosen among single choice, multiple choice, fill in the blank, text, drop-down, cascade, upload, and score question types, and adapt to both practice and questionnaire modes. 
Cascade questions, allow options to nest questions to display
Options are in html format
delete, view, search, and update questions
add, delete, modify, and view templates
add, delete, modify, and check projects(test/questionnaire) 
unified configuration of projects (whether it is open, whether a password is required, whether the question number is displayed, whether the progress bar is displayed, whether it is automatically saved, whether it is allowed to change the answer (i.e. redo), whether the answer sheet is visible, whether copying questions is allowed, whether the answer and analysis are displayed, start time, end time, if the questionnaire (test paper) is randomly generated, the number of random questions, question types, and which labels can be returned if the randomness is not enough, and if it is enough, the questions are randomly generated. 
Get the test paper and post the answer 
If it is a questionnaire, add reward points 
return the percentage of a certain item, the correct answer, whether it is correct, the answer analysis, etc. 
view your answers, whether it is correct, the answer, what proportion of people has chosen, etc.
administrators analyze the test paper, display the correct rate, and check each question. Fill-in-the-blank questions display the proportion of filled text, and multiple-choice questions display the proportion of options.

## API Documents
use knife4j and swagger to automatically export all interface API documents

## Customer Service
write some FAQs, allow adding, deleting, disabling questions, modifying similar questions, and displaying FAQs 
use Hanlp algorithm to extract keywords, count word frequency, calculate model parameters, inverse document frequency, calculate similarity between questions, use 0.19 as the boundary, distinguish similar questions from non-similar questions, complete automatic answers to questions, or answer "cannot answer".

## Courses
course purchase, the user is bound to the document after the points are deducted 
recent reading, record the different documents or videos read in the last five times
count the number of views of the course, sort the courses by popularity according to the number of views
popular courses
recommend courses according to course tags
course comment
add, delete, modify and check live broadcasts
classify by live broadcast, preview, and end of live broadcast
use video breakpoints to complete progress bar pull and fast forward

## Safety
supports both HTTP and HTTPS, configures HTTP connector, redirects HTTP to HTTPS
XSS cross-site scripting attack defense, modifies X-Frame-Options so that the address of the frame page can only be a page under the same domain name, preventing it from being embedded in other pages to prevent click hijacking
Response header, SQL injection filtering
