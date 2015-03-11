package com;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

public class AnalyzeSelect_TwoTablesWhereSupport 
{
	public void AnalyzeSelect(TSelectSqlStatement pStmt)
	{
		//The Query contains Where clause along with 2 tables.., Worst possible scenario
		TJoin join = pStmt.joins.getJoin(0);
		String table_name1 = join.getTable().toString();
		
		TJoin join1 = pStmt.joins.getJoin(1);
		
		String table_name2 = join1.getTable().toString();
		
		//Check if Table names are valid or not
		
		List<List<Integer>> all_data_table1 = new ArrayList<List<Integer>>(file1.data.get(table_name1));
		List<List<Integer>> all_data_table2 = new ArrayList<List<Integer>>(file1.data.get(table_name2));

		//Perform Cross Product of both tables
		List<String> all_col_names = new ArrayList<String>(); //This List contains the column names of cross product
		for(int b=0;b<file1.tables.get(table_name1).size();b++)
		{
			all_col_names.add(file1.tables.get(table_name1).get(b));
		}
		for(int b=0;b<file1.tables.get(table_name2).size();b++)
		{
			all_col_names.add(file1.tables.get(table_name2).get(b));
		}
		
		List<List<Integer>> cross_product = new ArrayList<List<Integer>>();
		for(int b=0;b<all_data_table1.size();b++)
		{
			for(int u=0;u<all_data_table2.size();u++)
			{
				List<Integer> inner_list1 = new ArrayList<Integer>(all_data_table1.get(b));
				List<Integer> inner_list2 = new ArrayList<Integer>(all_data_table2.get(u));
				for(int z=0;z<inner_list2.size();z++)
				{
					inner_list1.add(inner_list2.get(z));
				}
				cross_product.add(inner_list1);
			}
		}
		
		//System.out.print("54");
		//Checking if table exist or not
		if(all_data_table1==null||all_data_table2==null)
		{
			System.out.print("error");
			return;
		}
		
		//Prepare out_order and out_colnames
				List<Integer> out_order = new ArrayList<Integer>();
				List<String> out_colnames = new ArrayList<String>();
				
				for(int j=0; j < pStmt.getResultColumnList().size();j++)
				{
					TResultColumn resultColumn =pStmt.getResultColumnList().getResultColumn(j);
					String cur_column = resultColumn.getExpr().toString();
					boolean loc_validated=false;
					String table_name_ofcol = null;
					//validate cur_column
					if(cur_column.contains("."))
					{
						//The column name already contains the name of table,
						 table_name_ofcol  = cur_column.substring(0,cur_column.indexOf(".")-1);
						cur_column = cur_column.substring(cur_column.indexOf(".")+1);
						if(table_name_ofcol.equals(table_name1))
						{
							
							for(int b=0;b<file1.tables.get(table_name1).size();b++)
							{
								if(cur_column.equals(file1.tables.get(table_name1).get(b)))
								{
									loc_validated=true;
									break;
								}
							}
						}
						if(table_name_ofcol.equals(table_name2))
						{
							
							for(int b=0;b<file1.tables.get(table_name2).size();b++)
							{
								if(cur_column.equals(file1.tables.get(table_name2).get(b)))
								{
									loc_validated=true;
									break;
								}
							}
						}
					}
					else
					{
						for(int b=0;b<file1.tables.get(table_name1).size();b++)
						{
							if(cur_column.equals(file1.tables.get(table_name1).get(b)))
							{
								loc_validated=true;
								table_name_ofcol = table_name1;
								break;
							}
						}
						for(int b=0;b<file1.tables.get(table_name2).size();b++)
						{
							if(cur_column.equals(file1.tables.get(table_name2).get(b)))
							{
								loc_validated=true;
								table_name_ofcol = table_name2;
								break;
							}
						}
						
					}
					if(!loc_validated)
					{
						System.out.print("error");
						return;
					}
					out_colnames.add(table_name_ofcol+"."+cur_column);
					int push = Where_Helper.return_columnlocation(all_col_names, cur_column);
					//System.out.println("Pushing"+push+" for column: "+cur_column );
					out_order.add(push);	
				}

		
		//Check AND/OR and set appropriate flags
		boolean flag_and = false,flag_or=false;
		String where_token = pStmt.getWhereClause().getCondition().toString();
		String cond1,cond2;
		if(where_token.contains(" AND "))
		{
			flag_and = true;
			cond1 = where_token.substring(0,where_token.indexOf(" AND "));
			cond2 = where_token.substring(where_token.indexOf(" AND ")+5);
			
		}
		else if(where_token.contains(" and "))
		{
			flag_and = true;
			cond1 = where_token.substring(0,where_token.indexOf(" and "));
			cond2 = where_token.substring(where_token.indexOf(" and ")+5);
		
		}
		else if(where_token.contains(" And "))
		{
			flag_and = true;
			cond1 = where_token.substring(0,where_token.indexOf(" And "));
			cond2 = where_token.substring(where_token.indexOf(" And ")+5);
		
		}
		else if(where_token.contains(" OR "))
		{
			flag_or = true;
			cond1 = where_token.substring(0,where_token.indexOf(" OR "));
			cond2 = where_token.substring(where_token.indexOf(" OR ")+4);
		
			
		}
		else if(where_token.contains(" or "))
		{
			flag_or = true;
			cond1 = where_token.substring(0,where_token.indexOf(" or "));
			cond2 = where_token.substring(where_token.indexOf(" or ")+4);
		
		}
		else if(where_token.contains(" Or "))
		{
			flag_or = true;
			cond1 = where_token.substring(0,where_token.indexOf(" Or "));
			cond2 = where_token.substring(where_token.indexOf(" Or ")+4);
		
		}
		else
		{
			cond1 = where_token;
			cond2="";
		}
		
		List<List<Integer>> final_valid_data = new ArrayList<List<Integer>>();
		if(flag_and||flag_or)
		{
			//Two conditions
			int col1 = 0,col2=0,col3=0,col4=0;
			String op1_table = null,op2_table = null,op3_table=null,op4_table=null;
			boolean col2_isnum=false,col4_isnum=false;
			int op1=0,op2=0;
			String c1="",c2 = null,c3="",c4 = null;
			
			//Validating col1,col2;
			//Validate First Condition
			if(cond1.contains(">="))
			{
				StringTokenizer st = new StringTokenizer(cond1,">=");
				 c1=st.nextToken();
				 c2=st.nextToken();
				
				op1=Where_Helper.return_operatorindex(">=");
				
				
				
			}
			else if(cond1.contains("<="))
			{
				StringTokenizer st = new StringTokenizer(cond1,"<=");
				 c1=st.nextToken();
				 c2=st.nextToken();
				op1=Where_Helper.return_operatorindex("<=");
				
				
				
			}
			else if(cond1.contains("="))
			{
				StringTokenizer st = new StringTokenizer(cond1,"=");
				 c1=st.nextToken();
				 c2=st.nextToken();
				op1=Where_Helper.return_operatorindex("=");
				
				
				
			}
			else if(cond1.contains(">"))
			{
				StringTokenizer st = new StringTokenizer(cond1,">");
				 c1=st.nextToken();
				 c2=st.nextToken();
				op1=Where_Helper.return_operatorindex(">");
				
				
			}
			else if(cond1.contains("<"))
			{
				StringTokenizer st = new StringTokenizer(cond1,"<");
				 c1=st.nextToken();
				c2=st.nextToken();
				op1=Where_Helper.return_operatorindex("<");
				
				
			}
		
			//Assign values to table names of c1,c2
			 boolean validate_col1=false,validate_col2=false;
			 if(c1.contains("."))
			 {
				op1_table=c1.substring(0,c1.indexOf(".")-1);
				c1 = c1.substring(c1.indexOf(".")+1);
				int i = Where_Helper.return_columnlocation(file1.tables.get(op1_table), c1);
				if(i!=-1)
					validate_col1=true;
			 }
			 else
			 {
				
				 //Check in which table this columns exists
				 List<String> col_names = new ArrayList<String>(file1.tables.get(table_name1));
				 for(int b=0;b<col_names.size();b++)
				 {
					 if(c1.equals(col_names.get(b)))
					 {
						 validate_col1 = true;
						 op1_table=table_name1;
						 break;
					 }
				 }
				 List<String> col_names2 = new ArrayList<String>(file1.tables.get(table_name2));
				 for(int b=0;b<col_names2.size();b++)
				 {
					 if(c1.equals(col_names2.get(b)))
					 {
						 validate_col1 = true;
						 op1_table=table_name2;
						 break;
					 }
				 }
			
			 }
			 
			 if(c2.contains("."))
			 {
				op2_table=c2.substring(0,c2.indexOf(".")-1);
				c2 = c2.substring(c2.indexOf(".")+1);
				int i = Where_Helper.return_columnlocation(file1.tables.get(op2_table), c2);
				if(i!=-1)
					validate_col2=true;
			
			 }
			 else
			 {
				 //Check in which table this columns exists
				 List<String> col_names = new ArrayList<String>(file1.tables.get(table_name1));
				 for(int b=0;b<col_names.size();b++)
				 {
					 if(c1.equals(col_names.get(b)))
					 {
						 validate_col2 = true;
						 op2_table=table_name1;
						 break;
					 }
				 }
				 List<String> col_names2 = new ArrayList<String>(file1.tables.get(table_name2));
				 for(int b=0;b<col_names2.size();b++)
				 {
					 if(c1.equals(col_names2.get(b)))
					 {
						 validate_col2 = true;
						 op2_table=table_name2;
						 break;
					 }
				 }
			
			 }
			 
			 if(!validate_col1)
			 {
				 System.out.print("error");
				 return;
			 }
			 
			col1 = Where_Helper.return_columnlocation(all_col_names, c1);
			col2 = Where_Helper.return_columnlocation(all_col_names, c2);
			
			//Validate index values of col and op
			if(col1==-1||op1==-1)
			{
				System.out.print("error col1 op1");
				return ;
			}
			if(col2==-1)
			{
				try
				{
				
					col2=Integer.parseInt(c2);
					col2_isnum = true;
					validate_col2=true;
				}catch(Exception e)
				{
					System.out.print("error col2");
					return;
				}
				
			}
			if(!validate_col2)
			{
				System.out.print("error");
				return;
			}
			
			//Validating col3,col4
			//Validate First Condition
			if(cond2.contains(">="))
			{
				StringTokenizer st = new StringTokenizer(cond2,">=");
				 c3=st.nextToken();
				 c4=st.nextToken();
				
				op2=Where_Helper.return_operatorindex(">=");
				
				
				
			}
			else if(cond2.contains("<="))
			{
				StringTokenizer st = new StringTokenizer(cond2,"<=");
				 c3=st.nextToken();
				 c4=st.nextToken();
				op2=Where_Helper.return_operatorindex("<=");
				
				
				
			}
			else if(cond2.contains("="))
			{
				StringTokenizer st = new StringTokenizer(cond2,"=");
				 c3=st.nextToken();
				 c4=st.nextToken();
				op2=Where_Helper.return_operatorindex("=");
				
				
				
			}
			else if(cond2.contains(">"))
			{
				StringTokenizer st = new StringTokenizer(cond2,">");
				 c3=st.nextToken();
				 c4=st.nextToken();
				op2=Where_Helper.return_operatorindex(">");
				
				
			}
			else if(cond2.contains("<"))
			{
				StringTokenizer st = new StringTokenizer(cond2,"<");
				 c3=st.nextToken();
				 c4=st.nextToken();
				op2=Where_Helper.return_operatorindex("<");
				
				
			}
		
			//Assign values to table names of c1,c2
			 boolean validate_col3=false,validate_col4=false;
			 if(c3.contains("."))
			 {
				op3_table=c3.substring(0,c3.indexOf(".")-1);
				c3 = c3.substring(c3.indexOf(".")+1);
				int i = Where_Helper.return_columnlocation(file1.tables.get(op3_table), c3);
				if(i!=-1)
					validate_col3=true;
			 }
			 else
			 {
				
				 //Check in which table this columns exists
				 List<String> col_names = new ArrayList<String>(file1.tables.get(table_name1));
				 for(int b=0;b<col_names.size();b++)
				 {
					 if(c1.equals(col_names.get(b)))
					 {
						 validate_col3 = true;
						 op3_table=table_name1;
						 break;
					 }
				 }
				 List<String> col_names2 = new ArrayList<String>(file1.tables.get(table_name2));
				 for(int b=0;b<col_names2.size();b++)
				 {
					 if(c3.equals(col_names2.get(b)))
					 {
						 validate_col3 = true;
						 op3_table=table_name2;
						 break;
					 }
				 }
			
			 }
			 
			 if(c4.contains("."))
			 {
				op4_table=c4.substring(0,c4.indexOf(".")-1);
				c4 = c4.substring(c4.indexOf(".")+1);
				int i = Where_Helper.return_columnlocation(file1.tables.get(op4_table), c4);
				if(i!=-1)
					validate_col4=true;
			
			 }
			 else
			 {
				 //Check in which table this columns exists
				 List<String> col_names = new ArrayList<String>(file1.tables.get(table_name1));
				 for(int b=0;b<col_names.size();b++)
				 {
					 if(c4.equals(col_names.get(b)))
					 {
						 validate_col4 = true;
						 op4_table=table_name1;
						 break;
					 }
				 }
				 List<String> col_names2 = new ArrayList<String>(file1.tables.get(table_name2));
				 for(int b=0;b<col_names2.size();b++)
				 {
					 if(c4.equals(col_names2.get(b)))
					 {
						 validate_col4 = true;
						 op4_table=table_name2;
						 break;
					 }
				 }
			
			 }
			 
			 if(!validate_col3)
			 {
				 System.out.print("error");
				 return;
			 }
			 
			col3 = Where_Helper.return_columnlocation(all_col_names, c3);
			col4 = Where_Helper.return_columnlocation(all_col_names, c4);
			
			//Validate index values of col and op
			if(col3==-1||op2==-1)
			{
				System.out.print("error col1 op1");
				return ;
			}
			if(col4==-1)
			{
				try
				{
				
					col4=Integer.parseInt(c4);
					col4_isnum = true;
					validate_col4=true;
				}catch(Exception e)
				{
					System.out.print("error col2");
					return;
				}
				
			}
			if(!validate_col4)
			{
				System.out.print("error");
				return;
			}
		
			//Now Execute the condition and take the columns
			for(int b=0;b<cross_product.size();b++)
			{
				List<Integer> cur_list = cross_product.get(b);
				int oper2,oper1 = cur_list.get(col1);
				if(!col2_isnum)
					oper2=cur_list.get(col2);
				else
					oper2=col2;
				
				//As Per condition, evaluate expression
				boolean flag_cond1 = Where_Helper.check_condition_onop(oper1, oper2, op1);
				int oper4,oper3 = cur_list.get(col3);
				if(!col4_isnum)
					oper4=cur_list.get(col4);
				else
					oper4=col4;
				
				//As Per condition, evaluate expression
				boolean flag_cond2 = Where_Helper.check_condition_onop(oper3, oper4, op2);
				
				//If OR
				if(flag_or)
				{
					if(flag_cond1||flag_cond2)
						final_valid_data.add(cur_list);
				}
				
				else if(flag_and)
				{
					if(flag_cond1&&flag_cond2)
							
						final_valid_data.add(cur_list);
					
				}
			}
		
			//Finally Print the validated output
			Print_Helper.print_datafromout_twotableswithwhere(final_valid_data, out_colnames, out_order);
		}
		else
		{
			//Single Condition only
			
			//Extract Column names
			int col1 = 0,col2=0;
			String op1_table = null,op2_table = null;
			//assign table names
			boolean flag_cond1=false;		
			boolean col2_isnum=false;
			int op1=0;
			String c1="",c2 = null;
			
			//Validate First Condition
			if(cond1.contains(">="))
			{
				StringTokenizer st = new StringTokenizer(cond1,">=");
				 c1=st.nextToken();
				 c2=st.nextToken();
				
				op1=Where_Helper.return_operatorindex(">=");
				
				
				
			}
			else if(cond1.contains("<="))
			{
				StringTokenizer st = new StringTokenizer(cond1,"<=");
				 c1=st.nextToken();
				 c2=st.nextToken();
				op1=Where_Helper.return_operatorindex("<=");
				
				
				
			}
			else if(cond1.contains("="))
			{
				StringTokenizer st = new StringTokenizer(cond1,"=");
				 c1=st.nextToken();
				 c2=st.nextToken();
				op1=Where_Helper.return_operatorindex("=");
				
				
				
			}
			else if(cond1.contains(">"))
			{
				StringTokenizer st = new StringTokenizer(cond1,">");
				 c1=st.nextToken();
				 c2=st.nextToken();
				op1=Where_Helper.return_operatorindex(">");
				
				
			}
			else if(cond1.contains("<"))
			{
				StringTokenizer st = new StringTokenizer(cond1,"<");
				 c1=st.nextToken();
				c2=st.nextToken();
				op1=Where_Helper.return_operatorindex("<");
				
				
			}
		
			//Assign values to table names of c1,c2
			 boolean validate_col1=false,validate_col2=false;
			 if(c1.contains("."))
			 {
				op1_table=c1.substring(0,c1.indexOf(".")-1);
				c1 = c1.substring(c1.indexOf(".")+1);
				int i = Where_Helper.return_columnlocation(file1.tables.get(op1_table), c1);
				if(i!=-1)
					validate_col1=true;
			 }
			 else
			 {
				
				 //Check in which table this columns exists
				 List<String> col_names = new ArrayList<String>(file1.tables.get(table_name1));
				 for(int b=0;b<col_names.size();b++)
				 {
					 if(c1.equals(col_names.get(b)))
					 {
						 validate_col1 = true;
						 op1_table=table_name1;
						 break;
					 }
				 }
				 List<String> col_names2 = new ArrayList<String>(file1.tables.get(table_name2));
				 for(int b=0;b<col_names2.size();b++)
				 {
					 if(c1.equals(col_names2.get(b)))
					 {
						 validate_col1 = true;
						 op1_table=table_name2;
						 break;
					 }
				 }
			
			 }
			 
			 if(c2.contains("."))
			 {
				op2_table=c2.substring(0,c2.indexOf(".")-1);
				c2 = c2.substring(c2.indexOf(".")+1);
				int i = Where_Helper.return_columnlocation(file1.tables.get(op2_table), c2);
				if(i!=-1)
					validate_col2=true;
			
			 }
			 else
			 {
				 //Check in which table this columns exists
				 List<String> col_names = new ArrayList<String>(file1.tables.get(table_name1));
				 for(int b=0;b<col_names.size();b++)
				 {
					 if(c1.equals(col_names.get(b)))
					 {
						 validate_col2 = true;
						 op2_table=table_name1;
						 break;
					 }
				 }
				 List<String> col_names2 = new ArrayList<String>(file1.tables.get(table_name2));
				 for(int b=0;b<col_names2.size();b++)
				 {
					 if(c1.equals(col_names2.get(b)))
					 {
						 validate_col2 = true;
						 op2_table=table_name2;
						 break;
					 }
				 }
			
			 }
			 
			 if(!validate_col1)
			 {
				 System.out.print("error");
				 return;
			 }
			 
			col1 = Where_Helper.return_columnlocation(all_col_names, c1);
			col2 = Where_Helper.return_columnlocation(all_col_names, c2);
			
			//Validate index values of col and op
			if(col1==-1||op1==-1)
			{
				System.out.print("error col1 op1");
				return ;
			}
			if(col2==-1)
			{
				try
				{
				
					col2=Integer.parseInt(c2);
					col2_isnum = true;
					validate_col2=true;
				}catch(Exception e)
				{
					System.out.print("error col2");
					return;
				}
				
			}
			if(!validate_col2)
			{
				System.out.print("error");
				return;
			}
		
			//Now Execute the condition and take the columns
				for(int b=0;b<cross_product.size();b++)
				{
					List<Integer> cur_list = cross_product.get(b);
					int oper2,oper1 = cur_list.get(col1);
					if(!col2_isnum)
						oper2=cur_list.get(col2);
					else
						oper2=col2;
					
					//As Per condition, evaluate expression
					flag_cond1 = Where_Helper.check_condition_onop(oper1, oper2, op1);
					if(flag_cond1)
					{
						
							final_valid_data.add(cur_list);
						
					}
				}
			
				//Finally Print the validated output
				Print_Helper.print_datafromout_twotableswithwhere(final_valid_data, out_colnames, out_order);
		}
		
		
	}

}
