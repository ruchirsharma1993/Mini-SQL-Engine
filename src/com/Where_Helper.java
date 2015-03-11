package com;

import java.util.List;

public class Where_Helper 
{
	public static int return_columnlocation(List<String> out_colnames, String c)
	{
		//This function returns the column index of 'c' in out_colnames , else returns -1
		//System.out.print("Searching: "+c);
		for(int i=0;i<out_colnames.size();i++)
		{
			if(c.equals(out_colnames.get(i)))
				return i;
		}
		
		return -1;
	}
	public static int return_operatorindex(String c)
	{
		//This function returns corresponding index to operator, returns -1 if none of the indices are found
		
		if(c.equals("="))
			return 0;
		if(c.equals("<"))
			return 1;
		if(c.equals(">"))
			return 2;
		if(c.equals(">="))
			return 3;
		if(c.equals("<="))
			return 4;
		else
			return -1;
		
	}
	public static boolean check_condition_onop(int oper1,int oper2,int op1)
	{
		//System.out.println("In checkcond: "+oper1+":"+op1+":"+oper2);

		boolean flag_cond=false;
		if(op1==0)
		{
			if(oper1==oper2)
				flag_cond=true;
			
		}
		else if(op1==1)
		{
			if(oper1<oper2)
				flag_cond=true;
			
		}
		else if(op1==2)
		{
			if(oper1>oper2)
				flag_cond=true;
			
		}
		else if(op1==3)
		{
			if(oper1>=oper2)
				flag_cond=true;
			
		}
		else if(op1==4)
		{
			if(oper1<=oper2)
				flag_cond=true;
			
		}
		
		return flag_cond;
	}

}
