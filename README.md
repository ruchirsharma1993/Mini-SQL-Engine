------------------------------------------------
                    DATASET
------------------------------------------------
(1) A file named: metadata.txt would be given, which will have the following structure for each table:
<begin_table>
<table_name>
<attribute1>
....
<attributeN>
<end_table>

(2) csv files for tables. If a file is : File1.csv, the table name would be File1.

------------------------------------------------
                    INPUT FORMAT
------------------------------------------------

Command lines input such that: {compiled files} “SQL Query”. Here SQL Query would be a command line argument. 
Example : “select * from table_name where condition”

------------------------------------------------
               TYPE OF QUERIES
------------------------------------------------
This Mini SQL Engine handles the following set of queries: 

1. Select all records : Select * from table_name;

2. Aggregate functions: Simple aggregate functions on a single column. Sum, average, max and min. They will be very trivial given that the data is only numbers:  select max(col1) from table1;

3. Project Columns(could be any number of columns) from one or more tables : Select col1, col2 from table_name;

4. Select with distinct from one table : select distinct(col1),col2 from table_name;

5. Select with where from one or more tables: select
col1,col2 from table1,table2 where col1 = 10 AND col2
= 20; 
a. In the where queries, there would be a maximum
of one AND/OR operator with no NOT operators.

6. Projection of one or more(including all the columns) from two tables with one join condition :
a. select * from table1, table2 where table1.col1=table2.col2;
b. select col1,col2 from table1,table2 where table1.col1=table2.col2;
c. NO REPETITION OF COLUMNS – THE JOINING COLUMN SHOULD BE PRINTED ONLY ONCE.

7. Errors : If there are any sort of errors in the query like
no such table, wrong sql query etc, simply print
“error\n” on the console.

8. Empty sets : If a resulting query has no rows as
output, just print the column names.



------------------------------------------------
                    OUTPUT FORMAT
------------------------------------------------
The format of output is : comma-separated column names as the first row, followed by a new line character(‘\n’) and then the result row as comma-
separated. Two rows will be separated by a new line character.
