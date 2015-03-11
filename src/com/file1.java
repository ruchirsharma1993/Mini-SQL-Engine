package com;

import gudusoft.gsqlparser.*;

import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class file1 
{
	public static Map <String, List<String>> tables = new HashMap<String, List<String>>();
	public static Map <String, List<List<Integer>>> data = new HashMap<String,List<List<Integer>>>();
	public static String query;
	public static void main(String args[]) throws Exception
	{
		try
		{
			
			//Input Table from Metadata
			BufferedReader br = new BufferedReader(new FileReader("src/com/metadata.txt"));
			String next=br.readLine();
			
			
			while(next!=null&&next.equals("<begin_table>"))
			{
				
				
				String table_name = br.readLine();
				List <String> l = new ArrayList<String>();
				
				next=br.readLine();
				while(!next.equals("<end_table>"))
				{
					l.add(next);
					next=br.readLine();	
				}

				tables.put(table_name, l);
				
				
				//Read Contents of CSV File
				String csv_file = "src/com/"+table_name+".csv";
				List <List<Integer>> l1 = new ArrayList<List<Integer>>();
				//System.out.println("REading"+csv_file);
				BufferedReader br1 = new BufferedReader(new FileReader(csv_file));
				String read = br1.readLine();
				while(read!=null)
				{
					List <Integer> l2 = new ArrayList<Integer>();
					
					StringTokenizer st = new StringTokenizer(read,",");
					while(st.hasMoreElements())
					{
						String token = st.nextToken();
						if(token.startsWith("\""))
						{
							//System.out.println("Starts with double quotes");
							token = token.substring(1);
							token=token.substring(0, token.length()-1);
							int num = Integer.parseInt(token);
							l2.add(num);
							//System.out.println("token is: "+token);
						}
						else
						{
							int num = Integer.parseInt(token);
							//System.out.println("\t"+num);
							l2.add(num);
						}
					}
					l1.add(l2);
					read = br1.readLine();
				}
				data.put(table_name, l1);	//Inserted Content of csv file into data
				br1.close();
				next=br.readLine();
			}
			br.close();
			
			
			/***************************************************
			 		*PRE-PROCESSING COMPLETES HERE*
			 		*tables contains details of table
			 		*data contains data of corresponding table
			 ***************************************************/
			TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvoracle);
			
			//query = "Select A from table1,table3 where A>I";			
			//sqlparser.sqltext = query;
			sqlparser.sqltext=""+args[0]+"";
			int ret = sqlparser.parse();
			if(ret == 0)
			{
				for(int i=0;i<sqlparser.sqlstatements.size();i++)
				{
					analyzeStmt(sqlparser.sqlstatements.get(i));
					//System.out.println("");
				}
			}
			else
			{
			
				System.out.println(sqlparser.getErrormessage());
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception !! "+e.getMessage());
		}
	
	}

	
	/***************************************************
		*Support Function
	***************************************************/
	
	protected static void analyzeStmt(TCustomSqlStatement stmt)
	{
		//This function checks for the type of SQL Query  and passes control to corresponding function's file
		switch(stmt.sqlstatementtype)
		{
			case sstselect:
				AnalyzeSelect as = new AnalyzeSelect();
				as.analyzeSelectStmt((TSelectSqlStatement)stmt);
				break;
			case sstupdate:
				break;
			case sstcreatetable:
				break;
			case sstaltertable:
				break;
			case sstcreateview:
				break;
			default:
				System.out.println(stmt.sqlstatementtype.toString());
		}

	}
	
	
	
	
	
	
}


