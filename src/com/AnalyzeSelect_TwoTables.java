package com;

import gudusoft.gsqlparser.nodes.TJoin;

import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

import java.util.ArrayList;
import java.util.List;


public class AnalyzeSelect_TwoTables 
{
	public static void AnalyzeSelect(TSelectSqlStatement pStmt)
	{
		TJoin join = pStmt.joins.getJoin(0);
		String table_name1 = join.getTable().toString();
		
		TJoin join1 = pStmt.joins.getJoin(1);
		
		String table_name2 = join1.getTable().toString();
		
		//Check if Table names are valid or not
		
		List<List<Integer>> all_data_table1 = new ArrayList<List<Integer>>(file1.data.get(table_name1));
		List<List<Integer>> all_data_table2 = new ArrayList<List<Integer>>(file1.data.get(table_name2));
		if(all_data_table1==null||all_data_table2==null)
		{
			System.out.print("error");
			return;
		}
		
		//Retrieve selected columns from all data
		boolean flag_star=false;
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
	
		
		List<List<Integer>> selected_data_table1;
		List<List<Integer>> selected_data_table2;
		
		List<String> out_colnames = new ArrayList<String>();
		//If * then cross product, no need to retrieve any column directly for cross product of all_data
		if(flag_star)
		{
			//Start exist, check only for WHERE clause
			selected_data_table1= new ArrayList<List<Integer>>(all_data_table1);
			selected_data_table2=  new ArrayList<List<Integer>>(all_data_table2);
			for(int u=0;u<file1.tables.get(table_name1).size();u++)
			{
				out_colnames.add(table_name1+"."+file1.tables.get(table_name1).get(u));
			}
			for(int u=0;u<file1.tables.get(table_name2).size();u++)
			{
				out_colnames.add(table_name2+"."+file1.tables.get(table_name2).get(u));
			}
		}
		else
		{
			selected_data_table1= new ArrayList<List<Integer>>();
			selected_data_table2=  new ArrayList<List<Integer>>();
			
			for(int j=0; j < pStmt.getResultColumnList().size();j++)
			{
				
				
				TResultColumn resultColumn =pStmt.getResultColumnList().getResultColumn(j);
				String cur_column = resultColumn.getExpr().toString();
				
				
				//System.out.println("Checking "+cur_column+" in "+table_name1+" : "+table_name2);
				//Check if this column name exist in table1
				boolean validate_cur_column=false;
				if(cur_column.contains("."))
				{
					cur_column=cur_column.substring(cur_column.indexOf(".")+1);
					if(cur_column.contains(table_name1+"."))
					{
						for(int u=0;u<file1.tables.get(table_name1).size();u++)
						{
							if(file1.tables.get(table_name1).get(u).equals(cur_column))
							{
								for(int y=0;y<all_data_table1.size();y++)
								{
									//System.out.println("96");
									List<Integer> cur_local_list = all_data_table1.get(y);
									if(selected_data_table1.size()<=y)
									{
										//System.out.println("ere");
										List<Integer> new_local_list = new ArrayList<Integer>();
										
										new_local_list.add(cur_local_list.get(u));
										//System.out.println("98");
										selected_data_table1.add(new_local_list);
									}
									else
										selected_data_table1.get(y).add(cur_local_list.get(u));
								}
								//selected_data_table1.add(all_data_table1.get(u));
								String col_loc_det = table_name1+"."+cur_column;
								out_colnames.add(col_loc_det);
								validate_cur_column=true;
								break;
							}
						}
					}
					else if(cur_column.contains(table_name2+"."))
					{
						for(int u=0;u<file1.tables.get(table_name2).size();u++)
						{
							if(file1.tables.get(table_name2).get(u).equals(cur_column))
							{
								for(int y=0;y<all_data_table2.size();y++)
								{
									List<Integer> cur_local_list = all_data_table2.get(y);
									if(selected_data_table2.size()<=y)
									{
										List<Integer> new_local_list = new ArrayList<Integer>();
										new_local_list.add(cur_local_list.get(u));
										selected_data_table2.add(new_local_list);
									}
									else
										selected_data_table2.get(y).add(cur_local_list.get(u));
								}
								String col_loc_det = table_name2+"."+cur_column;
								out_colnames.add(col_loc_det);
								validate_cur_column=true;
								break;
							}
						}
						
					
					}
				}
				else
				{
					for(int u=0;u<file1.tables.get(table_name1).size();u++)
					{
						if(file1.tables.get(table_name1).get(u).equals(cur_column))
						{
							for(int y=0;y<all_data_table1.size();y++)
							{
								//System.out.println("96");
								List<Integer> cur_local_list = all_data_table1.get(y);
								if(selected_data_table1.size()<=y)
								{
									//System.out.println("ere");
									List<Integer> new_local_list = new ArrayList<Integer>();
									
									new_local_list.add(cur_local_list.get(u));
									//System.out.println("98");
									selected_data_table1.add(new_local_list);
								}
								else
									selected_data_table1.get(y).add(cur_local_list.get(u));
							}
							//selected_data_table1.add(all_data_table1.get(u));
							String col_loc_det = table_name1+"."+cur_column;
							out_colnames.add(col_loc_det);
							validate_cur_column=true;
							break;
						}
					}
					if(!validate_cur_column)
					{
						for(int u=0;u<file1.tables.get(table_name2).size();u++)
						{
							if(file1.tables.get(table_name2).get(u).equals(cur_column))
							{
								for(int y=0;y<all_data_table2.size();y++)
								{
									List<Integer> cur_local_list = all_data_table2.get(y);
									if(selected_data_table2.size()<=y)
									{
										List<Integer> new_local_list = new ArrayList<Integer>();
										new_local_list.add(cur_local_list.get(u));
										selected_data_table2.add(new_local_list);
									}
									else
										selected_data_table2.get(y).add(cur_local_list.get(u));
								}
								String col_loc_det = table_name2+"."+cur_column;
								out_colnames.add(col_loc_det);
								validate_cur_column=true;
								break;
							}
						}
						
					}
				}
				//If Current Column not validated, then print error message and Exit
				if(!validate_cur_column)
				{
					System.out.print("error col");
					return;
				}
			
			}
		}
		
	
		
		//System.out.println("143");
		
		//Tables Exist so proceed further for Cross Product of two tables
		List<List<Integer>> all_data = new ArrayList<List<Integer>>();
		for(int i=0;i<selected_data_table1.size();i++)
		{
			for(int j=0;j<selected_data_table2.size();j++)
			{	
				//loc_list.add(all_data_table1.get(i));
				List<Integer> loc_list = new ArrayList<Integer>(selected_data_table1.get(i));
				List<Integer> inner_list = new ArrayList<Integer>(selected_data_table2.get(j));
				for(int k=0;k<inner_list.size();k++)
				{
					loc_list.add(inner_list.get(k));
					//System.out.print("Add: "+inner_list.get(k));
				}
				//System.out.println("Inner loop");
				all_data.add(loc_list);
				
			}
			//System.out.println("Next: ");
		}
		
		
	//Finally Print output
		Print_Helper.print_datafromout_twotables(out_colnames, all_data);
	}
	

}
