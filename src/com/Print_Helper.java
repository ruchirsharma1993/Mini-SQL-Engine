package com;


import java.util.List;

public class Print_Helper 
{
	public static void print_datafromout_singletable(List<String> out_colnames,List<List<Integer>>out_values,String table_name)
	{
		for(int k=0;k<out_colnames.size();k++)
		{
			System.out.print(table_name+"."+out_colnames.get(k));
			if(k!=out_colnames.size()-1)
				System.out.print(",");
			else
				System.out.print("\n");
		}
		
		for(int k=0;k<out_values.size();k++)
		{
			List<Integer> l = out_values.get(k);
			//System.out.println(l);
			for(int b=0;b<l.size();b++)
			{
				System.out.print(l.get(b));
				if(b!=out_colnames.size()-1)
					System.out.print(",");
				
			}
			if(k!=out_values.size()-1)
				System.out.print("\n");
		}
	}
	
	public static void print_datafromout_twotableswithwhere(List<List<Integer>> final_valid_data,List<String> out_colnames,List<Integer> out_order)
	{
		//Print out_colnames first
		for(int k=0;k<out_colnames.size();k++)
		{
			System.out.print(out_colnames.get(k));
			if(k!=out_colnames.size()-1)
				System.out.print(",");
			else
				System.out.print("\n");
		}
		
		for(int k=0;k<final_valid_data.size();k++)
		{
			for(int u=0;u<out_order.size();u++)
			{
				System.out.print(final_valid_data.get(k).get(out_order.get(u)));
				if(u!=out_order.size()-1)
					System.out.print(",");
				
			}
			if(k!=final_valid_data.size()-1)
				System.out.print("\n");
		}
			
	}
	public static void print_datafromout_twotables(List<String> out_colnames,List<List<Integer>>out_values)
	{
		for(int k=0;k<out_colnames.size();k++)
		{
			System.out.print(out_colnames.get(k));
			if(k!=out_colnames.size()-1)
				System.out.print(",");
			else
				System.out.print("\n");
		}
		
		for(int k=0;k<out_values.size();k++)
		{
			List<Integer> l = out_values.get(k);
			//System.out.println(l);
			for(int b=0;b<l.size();b++)
			{
				System.out.print(l.get(b));
				if(b!=out_colnames.size()-1)
					System.out.print(",");
				
			}
			if(k!=out_values.size()-1)
				System.out.print("\n");
		}
	}

}
