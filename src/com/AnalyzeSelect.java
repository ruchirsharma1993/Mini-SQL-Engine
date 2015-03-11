package com;

import gudusoft.gsqlparser.TBaseType;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class AnalyzeSelect {


	public void analyzeSelectStmt(TSelectSqlStatement pStmt)
	{
		//System.out.println("\nSelect statement:");
		if (pStmt.isCombinedQuery())
		{
	
		}
		
		else
		{
		
			if(pStmt.joins.size()==1)
			{
				//This part works for Single Table's only
				boolean flag_star=false,flag_aggr=false,flag_distinct=false;;
				TJoin join = pStmt.joins.getJoin(0);
				switch(join.getKind())
				{
					case TBaseType.join_source_fake:
					{
						//Single Table, checking for columns:
						String table_name = join.getTable().toString();
						List<String> check=file1.tables.get(table_name);
						if(check==null)
						{
							//Error CASE as table doesn't exist
							System.out.println("error table doesnt exist\n");
						}
						if(pStmt.getResultColumnList().size()==1)
						{
							//Single Column, check for *
							TResultColumn resultColumn =pStmt.getResultColumnList().getResultColumn(0);
							String oper = resultColumn.getExpr().toString();
							if(oper.toString().equals("*"))
							{
								flag_star=true;
							}
							
							
						}	
								
						//SELECT ALL FROM CURRENT TABLE AND DISPLAY
								
								
						if(flag_star)		
						{	
							//Output * for table name
									
							//First Take Output data into out_colnames and out_values
							
							List<String> out_colnames = new ArrayList<String>();
							List<List<Integer>>out_values = new ArrayList<List<Integer>>();
							for(int k=0;k<check.size();k++)
							{
								out_colnames.add(check.get(k));
							}
							List<List<Integer>> output = file1.data.get(table_name);
							for(int k=0;k<output.size();k++)
							{
								List<Integer> l = output.get(k);
								out_values.add(l);
							}
							
							//check for where condition
							if(pStmt.getWhereClause() != null)
							{
								//Query contains where clause,
								//System.out.printf("\n where clause: \n\t%s\n",pStmt.getWhereClause().getCondition().toString());
								
								//Breaking Where Clause
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
								
								int col1 = 0,col2=0,col3=0,col4=0;
								
								boolean col2_isnum=false,col4_isnum=false;
								int op1=0,op2=0;
								String c1,c2 = null,c3,c4 = null;
								
								//Validate First Condition
								if(cond1.contains(">="))
								{
									StringTokenizer st = new StringTokenizer(cond1,">=");
									 c1=st.nextToken();
									 c2=st.nextToken();
									col1 = Where_Helper.return_columnlocation(out_colnames, c1);
									col2 = Where_Helper.return_columnlocation(out_colnames, c2);
									op1=Where_Helper.return_operatorindex(">=");
									
									
									
								}
								else if(cond1.contains("<="))
								{
									StringTokenizer st = new StringTokenizer(cond1,"<=");
									 c1=st.nextToken();
									 c2=st.nextToken();
									col1 = Where_Helper.return_columnlocation(out_colnames, c1);
									col2 = Where_Helper.return_columnlocation(out_colnames, c2);
									op1=Where_Helper.return_operatorindex("<=");
									
									
									
								}
								else if(cond1.contains("="))
								{
									StringTokenizer st = new StringTokenizer(cond1,"=");
									 c1=st.nextToken();
									 c2=st.nextToken();
									col1 = Where_Helper.return_columnlocation(out_colnames, c1);
									col2 = Where_Helper.return_columnlocation(out_colnames, c2);
									op1=Where_Helper.return_operatorindex("=");
									
									
									
								}
								else if(cond1.contains(">"))
								{
									StringTokenizer st = new StringTokenizer(cond1,">");
									 c1=st.nextToken();
									 c2=st.nextToken();
									col1 = Where_Helper.return_columnlocation(out_colnames, c1);
									col2 = Where_Helper.return_columnlocation(out_colnames, c2);
									op1=Where_Helper.return_operatorindex(">");
									
									
								}
								else if(cond1.contains("<"))
								{
									StringTokenizer st = new StringTokenizer(cond1,"<");
									 c1=st.nextToken();
									c2=st.nextToken();
									col1 = Where_Helper.return_columnlocation(out_colnames, c1);
									col2 = Where_Helper.return_columnlocation(out_colnames, c2);
									op1=Where_Helper.return_operatorindex("<");
									
									
								}
							
								//Validate index values of col and op
								if(col1==-1||op1==-1)
								{
									System.out.print("error");
									return ;
								}
								if(col2==-1)
								{
									try
									{
									
										col2=Integer.parseInt(c2);
										col2_isnum = true;
									}catch(Exception e)
									{
										System.out.print("error");
										return;
									}
									
								}
								
								//Final Output of this query to be put in final_outvalues
								List<List<Integer>> final_outvalues = new ArrayList<List<Integer>>();
								
								if(flag_and||flag_or)
								{
								
									//System.out.print("ANDOR");
									//AND or OR Exist in query, so evaluate second condition
									if(cond2.contains(">="))
									{
										StringTokenizer st = new StringTokenizer(cond2,">=");
										 c3=st.nextToken();
										 c4=st.nextToken();
										col3 = Where_Helper.return_columnlocation(out_colnames, c3);
										col4 = Where_Helper.return_columnlocation(out_colnames, c4);
										op2=Where_Helper.return_operatorindex(">=");
										
										
									}
									else if(cond2.contains("<="))
									{
										StringTokenizer st = new StringTokenizer(cond2,"<=");
										 c3=st.nextToken();
										 c4=st.nextToken();
										col3 = Where_Helper.return_columnlocation(out_colnames, c3);
										col4 = Where_Helper.return_columnlocation(out_colnames, c4);
										op2=Where_Helper.return_operatorindex("<=");
										
										
									}
									else if(cond2.contains("="))
									{
										StringTokenizer st = new StringTokenizer(cond2,"=");
										 c3=st.nextToken();

										// System.out.print("288");
										 c4=st.nextToken();
										// System.out.print("290");
										col3 = Where_Helper.return_columnlocation(out_colnames, c3);
										col4 = Where_Helper.return_columnlocation(out_colnames, c4);
										op2=Where_Helper.return_operatorindex("=");
										
										
									}
									else if(cond2.contains(">"))
									{
										StringTokenizer st = new StringTokenizer(cond2,">");
										 c3=st.nextToken();
										 c4=st.nextToken();
										col3 = Where_Helper.return_columnlocation(out_colnames, c3);
										col4 = Where_Helper.return_columnlocation(out_colnames, c4);
										op2=Where_Helper.return_operatorindex(">");
										
										
									}
									else if(cond2.contains("<"))
									{
										StringTokenizer st = new StringTokenizer(cond2,"<");
										 c3=st.nextToken();
										 c4=st.nextToken();
										col3 = Where_Helper.return_columnlocation(out_colnames, c3);
										col4 = Where_Helper.return_columnlocation(out_colnames, c4);
										op2=Where_Helper.return_operatorindex("<");
										
										
									}
									
									//Validate index values of col and op
									if(col3==-1||op2==-1)
									{
										System.out.print("error");
										return ;
									}
									if(col4==-1)
									{
										try
										{
										
											col4=Integer.parseInt(c4);
											col4_isnum = true;
										}catch(Exception e)
										{
											System.out.print("error");
											return;
										}
										
									}
								
								
							
								
									//System.out.print("Validatedtill 340");
									for(int b=0;b<out_values.size();b++)
									{
										List<Integer> cur_list = out_values.get(b);
										
										//Checking Condition One
										int oper1,oper2,oper3,oper4;
										oper1  = cur_list.get(col1);
										if(col2_isnum)
											oper2=col2;
										else
											oper2=cur_list.get(col2);
										
										oper3  = cur_list.get(col3);
										if(col4_isnum)
											oper4=col4;
										else
											oper4=cur_list.get(col4);
										boolean flag_cond1=false,flag_cond2=false;
										flag_cond1 = Where_Helper.check_condition_onop(oper1, oper2, op1);
										flag_cond2 = Where_Helper.check_condition_onop(oper3, oper4, op2);
										if(flag_and)
										{
											if(flag_cond1&&flag_cond2)
											final_outvalues.add(cur_list);
										}
										else if(flag_or)
										{
											if(flag_cond1||flag_cond2)
												final_outvalues.add(cur_list);
											
										}
								
									}
								
									
								}
								else
								{
									//Only One Condition Exist, so evaluate only for cond1
									for(int b=0;b<out_values.size();b++)
									{
										List<Integer> cur_list = out_values.get(b);
										
										//Checking Condition One
										int oper1,oper2;
										oper1  = cur_list.get(col1);
										if(col2_isnum)
											oper2=col2;
										else
											oper2=cur_list.get(col2);
										
										
										boolean flag_cond1=false;
										flag_cond1 = Where_Helper.check_condition_onop(oper1, oper2, op1);
										
									
										if(flag_cond1)
												final_outvalues.add(cur_list);
											
								
									}
								
								
								}
								
								//Finally Print The data for the query

								Print_Helper.print_datafromout_singletable(out_colnames, final_outvalues, table_name);
							
							
							
							}
							else
							{
								//Query is simple and doesnt contains Where clause, so call print function from helper class directly..
								
								Print_Helper.print_datafromout_singletable(out_colnames, out_values, table_name);
							
							}
						}	
						
						else
						{
							///Not * column, select limited columns
							boolean flag_checkcolumn=true;
							List<Integer> out_order = new ArrayList<Integer>();
							
							
							/*
							 * This loop checks column names and existence of Distinct, Aggregate Function etc queries
							 * */
							for(int j=0; j < pStmt.getResultColumnList().size();j++)
							{
								boolean loc_flag_distinct=false;
								
								TResultColumn resultColumn =pStmt.getResultColumnList().getResultColumn(j);
								String cur_column = resultColumn.getExpr().toString();
								if(cur_column.contains("max(")||cur_column.contains("MAX(")||cur_column.contains("Max("))
									flag_aggr=true;
								else if(cur_column.contains("sum(")||cur_column.contains("SUM(")||cur_column.contains("Sum("))
									flag_aggr=true;
								else if(cur_column.contains("AVG(")||cur_column.contains("avg(")||cur_column.contains("Avg("))
									flag_aggr=true;
								else if(cur_column.contains("min(")||cur_column.contains("MIN(")||cur_column.contains("Min("))
									flag_aggr=true;
								else if(file1.query.contains("DISTINCT"+cur_column)||file1.query.contains("distinct"+cur_column)||file1.query.contains("Distinct"+cur_column))
									loc_flag_distinct=true;
								
								if(flag_aggr||loc_flag_distinct)
								{
									cur_column = cur_column.substring(cur_column.indexOf("(")+1);
									cur_column = cur_column.substring(0,cur_column.length()-1);
									
								}
							
								boolean loc_flag_checkcolumn = false;
								for(int k=0;k<check.size();k++)
								{
										
									
									if(check.get(k).equals(cur_column))
									{
										loc_flag_checkcolumn=true;
										out_order.add(k);
										break;
									}								
										
								}
								if(loc_flag_distinct)
									flag_distinct=true;
								if(!loc_flag_checkcolumn)
								{
									flag_checkcolumn = false;
									break;
								}
							}
								
							//Check flag value, if any column name in query is invalid print error else display output
							if(!flag_checkcolumn)
								System.out.println("error invalid col name in query");
							else if(flag_aggr)
							{
								//IF given query contains aggregate function
								List<String> out_colnames = new ArrayList<String>();
								List<Integer>out_values = new ArrayList<Integer>();
								for(int j=0; j < pStmt.getResultColumnList().size();j++)
								{
									TResultColumn resultColumn =pStmt.getResultColumnList().getResultColumn(j);
									String cur_column = resultColumn.getExpr().toString();
									out_colnames.add(cur_column);
									
									boolean flag_max=false,flag_min=false,flag_sum=false,flag_avg=false;
									if(cur_column.contains("max(")||cur_column.contains("MAX(")||cur_column.contains("Max("))
										flag_max=true;
									else if(cur_column.contains("sum(")||cur_column.contains("SUM(")||cur_column.contains("Sum("))
										flag_sum=true;
									else if(cur_column.contains("AVG(")||cur_column.contains("avg(")||cur_column.contains("Avg("))
										flag_avg=true;
									else if(cur_column.contains("min(")||cur_column.contains("MIN(")||cur_column.contains("Min("))
										flag_min=true;
									cur_column = cur_column.substring(cur_column.indexOf("(")+1);
									cur_column = cur_column.substring(0,cur_column.length()-1);
									if(cur_column.contains("."))
										cur_column=cur_column.substring(cur_column.indexOf(".")+1);
									//Get index of corresponding column from tables Hashmap
									List<String> col_details = file1.tables.get(table_name);
									int index_column=-1;
									for(int b=0;b<col_details.size();b++)
									{
										if(col_details.get(b).equals(cur_column))
										{
											index_column = b;
											break;
										}
									}
									List<List<Integer>> all_data = file1.data.get(table_name);
									
									//Cur is list to store all the values of column
									List<Integer> cur = new ArrayList<Integer>();
									for(int b=0;b<all_data.size();b++)
									{
										List<Integer> local_list = all_data.get(b);
										cur.add(local_list.get(index_column));
									}
									
									//Now we have the list of data of a particular column and the corresponding function (Min,max,ave,sum).
									//Call Appropriate Function
									if(flag_min)
										out_values.add(AggrHelper.aggrfunc_findmin(cur));
									else if(flag_max)
										out_values.add(AggrHelper.aggrfunc_findmax(cur));
									else if(flag_avg)
										out_values.add(AggrHelper.aggrfunc_findavg(cur));
									else if(flag_sum)
										out_values.add(AggrHelper.aggrfunc_findsum(cur));
								}
								
								//Finally Print the output for Aggregate Function
								for(int b=0;b<out_colnames.size();b++)
								{
									System.out.print(out_colnames.get(b));
									if(b!=out_colnames.size()-1)
										System.out.print(",");
									else
										System.out.print("\n");
								}
								for(int b=0;b<out_values.size();b++)
								{
									System.out.print(out_values.get(b));
									if(b!=out_values.size()-1)
										System.out.print(",");
										
								}
								
							}
							else if(flag_distinct)
							{
								//Query contains distinct keyword, process accordingly..!!
								Set<List<Integer>> out = new HashSet<>();
								
								for(int j=0; j < pStmt.getResultColumnList().size();j++)
								{
									TResultColumn resultColumn =pStmt.getResultColumnList().getResultColumn(j);
									String cur_column = resultColumn.getExpr().toString();
									
									//Validate Column Name, remove ( and )
									if(cur_column.startsWith("("))
									{
										cur_column = cur_column.substring(cur_column.indexOf("(")+1);
										cur_column = cur_column.substring(0,cur_column.length()-1);
									}
									if(cur_column.contains("."))
										cur_column=cur_column.substring(cur_column.indexOf(".")+1);
									//Print Column Name's
									System.out.print(cur_column);
									if(j!=pStmt.getResultColumnList().size()-1)
										System.out.print(",");
								}
								
								//Collect Data from Master Record
								List<List<Integer>> all_data = file1.data.get(table_name);
								for(int b=0;b<all_data.size();b++)
								{
									List<Integer> cur = all_data.get(b);
									List<Integer>inside_list = new ArrayList<Integer>();
									for(int l=0;l<out_order.size();l++)
									{
										int out_next = out_order.get(l);
										int next_int_toadd = cur.get(out_next);
										inside_list.add(next_int_toadd);
									}
									out.add(inside_list);
									
								}
								
								
								//Finally Print output from out list
								for (List<Integer> inside_list : out)
								{
									System.out.print("\n");
								    for(int b=0;b<inside_list.size();b++)
								    {
								    	System.out.print(inside_list.get(b));
								    	if(b!=inside_list.size()-1)
								    		System.out.print(",");
								    	
								    }
								}
							}
							else
							{
								/*
								 * THE SIMPLE Select col1,col2 query. May contain "where"
								 * */
								
								//List columns first
								List<String> out_colnames = new ArrayList<String>();
								for(int j=0; j < pStmt.getResultColumnList().size();j++)
								{
									TResultColumn resultColumn =pStmt.getResultColumnList().getResultColumn(j);
									String cur_column = resultColumn.getExpr().toString();
									out_colnames.add(cur_column);
										
								}
								//List Output
								List<List<Integer>> all_data = file1.data.get(table_name);
								//Check for while condition
								if(pStmt.getWhereClause() != null)
								{
									//Query contains where clause,
									//System.out.printf("\n where clause: \n\t%s\n",pStmt.getWhereClause().getCondition().toString());
									
									//Breaking Where Clause
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
									
									int col1 = 0,col2=0,col3=0,col4=0;
									
									boolean col2_isnum=false,col4_isnum=false;
									int op1=0,op2=0;
									String c1,c2 = null,c3,c4 = null;
									
									//Validate First Condition
									if(cond1.contains(">="))
									{
										StringTokenizer st = new StringTokenizer(cond1,">=");
										 c1=st.nextToken();
										 c2=st.nextToken();
										col1 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c1);
										col2 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c2);
										op1=Where_Helper.return_operatorindex(">=");
										
										
										
									}
									else if(cond1.contains("<="))
									{
										StringTokenizer st = new StringTokenizer(cond1,"<=");
										 c1=st.nextToken();
										 c2=st.nextToken();
										col1 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c1);
										col2 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c2);
										op1=Where_Helper.return_operatorindex("<=");
										
										
										
									}
									else if(cond1.contains("="))
									{
										StringTokenizer st = new StringTokenizer(cond1,"=");
										 c1=st.nextToken();
										 c2=st.nextToken();
										col1 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c1);
										col2 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c2);
										op1=Where_Helper.return_operatorindex("=");
										
										
										
									}
									else if(cond1.contains(">"))
									{
										StringTokenizer st = new StringTokenizer(cond1,">");
										 c1=st.nextToken();
										 c2=st.nextToken();
										col1 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c1);
										col2 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c2);
										op1=Where_Helper.return_operatorindex(">");
										
										
									}
									else if(cond1.contains("<"))
									{
										StringTokenizer st = new StringTokenizer(cond1,"<");
										 c1=st.nextToken();
										c2=st.nextToken();
										col1 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c1);
										col2 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c2);
										op1=Where_Helper.return_operatorindex("<");
										
										
									}
								
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
										}catch(Exception e)
										{
											System.out.print("error col2");
											return;
										}
										
									}
									
									//Final Output of this query to be put in final_outvalues
									List<List<Integer>> final_outvalues = new ArrayList<List<Integer>>();
									
									if(flag_and||flag_or)
									{
									
										//System.out.print("ANDOR");
										//AND or OR Exist in query, so evaluate second condition
										if(cond2.contains(">="))
										{
											StringTokenizer st = new StringTokenizer(cond2,">=");
											 c3=st.nextToken();
											 c4=st.nextToken();
											col3 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c3);
											col4 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c4);
											op2=Where_Helper.return_operatorindex(">=");
											
											
										}
										else if(cond2.contains("<="))
										{
											StringTokenizer st = new StringTokenizer(cond2,"<=");
											 c3=st.nextToken();
											 c4=st.nextToken();
											col3 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c3);
											col4 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c4);
											op2=Where_Helper.return_operatorindex("<=");
											
											
										}
										else if(cond2.contains("="))
										{
											StringTokenizer st = new StringTokenizer(cond2,"=");
											 c3=st.nextToken();

											 System.out.print("288");
											 c4=st.nextToken();
											 System.out.print("290");
											col3 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c3);
											col4 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c4);
											op2=Where_Helper.return_operatorindex("=");
											
											
										}
										else if(cond2.contains(">"))
										{
											StringTokenizer st = new StringTokenizer(cond2,">");
											 c3=st.nextToken();
											 c4=st.nextToken();
											col3 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c3);
											col4 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c4);
											op2=Where_Helper.return_operatorindex(">");
											
											
										}
										else if(cond2.contains("<"))
										{
											StringTokenizer st = new StringTokenizer(cond2,"<");
											 c3=st.nextToken();
											 c4=st.nextToken();
											col3 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c3);
											col4 = Where_Helper.return_columnlocation(file1.tables.get(table_name), c4);
											op2=Where_Helper.return_operatorindex("<");
											
											
										}
										
										//Validate index values of col and op
										if(col3==-1||op2==-1)
										{
											System.out.print("error col3 op2");
											return ;
										}
										if(col4==-1)
										{
											try
											{
											
												col4=Integer.parseInt(c4);
												col4_isnum = true;
											}catch(Exception e)
											{
												System.out.print("error c4");
												return;
											}
											
										}
									
									
								
									
										//System.out.print("Validated till 340");
										for(int b=0;b<all_data.size();b++)
										{
											List<Integer> cur_list = all_data.get(b);
											List<Integer>fin_topush = new ArrayList<Integer>();
											
											//Process Cur_list as per out_order
											for(int j=0;j<out_order.size();j++)
											{
												int next_indexofcol = out_order.get(j);
												fin_topush.add(cur_list.get(next_indexofcol));
											}
											
											//Checking Condition One
											int oper1,oper2,oper3,oper4;
											oper1  = cur_list.get(col1);
											if(col2_isnum)
												oper2=col2;
											else
												oper2=cur_list.get(col2);
											
											oper3  = cur_list.get(col3);
											if(col4_isnum)
												oper4=col4;
											else
												oper4=cur_list.get(col4);
											boolean flag_cond1=false,flag_cond2=false;
											flag_cond1 = Where_Helper.check_condition_onop(oper1, oper2, op1);
											flag_cond2 = Where_Helper.check_condition_onop(oper3, oper4, op2);
											if(flag_and)
											{
												if(flag_cond1&&flag_cond2)
												final_outvalues.add(fin_topush);
											}
											else if(flag_or)
											{
												if(flag_cond1||flag_cond2)
													final_outvalues.add(fin_topush);
												
											}
									
										}
									
										
									}
									else
									{
										//Only One Condition Exist, so evaluate only for cond1
										for(int b=0;b<all_data.size();b++)
										{
											List<Integer> cur_list = all_data.get(b);
											List<Integer>fin_topush = new ArrayList<Integer>();
											
											//Process Cur_list as per out_order
											for(int j=0;j<out_order.size();j++)
											{
												int next_indexofcol = out_order.get(j);
												fin_topush.add(cur_list.get(next_indexofcol));
											}
											//Checking Condition One
											int oper1,oper2;
											oper1  = cur_list.get(col1);
											if(col2_isnum)
												oper2=col2;
											else
												oper2=cur_list.get(col2);
											
											
											boolean flag_cond1=false;
											flag_cond1 = Where_Helper.check_condition_onop(oper1, oper2, op1);
											
										
											if(flag_cond1)
													final_outvalues.add(fin_topush);
												
									
										}
									
									
									}
									
									//Finally Print The data for the query

									Print_Helper.print_datafromout_singletable(out_colnames, final_outvalues, table_name);
									
								
								
								}
								else
								{
									//Select With multiple columns without "where" for single table
									for(int b=0;b<all_data.size();b++)
									{	
								
										List<Integer> cur = all_data.get(b);
										for(int l=0;l<out_order.size();l++)
										{
											int out_next = out_order.get(l);
											System.out.print(cur.get(out_next));
											if(l!=out_order.size()-1)
												System.out.print(",");
										}
										if(b!=all_data.size()-1)
											System.out.print("\n");
										
									}
									
									
								}
							
							}
								
							
						}
							
						
					}	
						
					
				}
					
				
			
			}
			else
			{
				if(pStmt.getWhereClause() != null)
				{
					AnalyzeSelect_TwoTablesWhereSupport as = new AnalyzeSelect_TwoTablesWhereSupport(); 
					as.AnalyzeSelect(pStmt);
				}
				else
				{
					//Call another class file function to process the query
					AnalyzeSelect_TwoTables.AnalyzeSelect(pStmt);
				}
			}
		}
		
		
		
	
	}


}
