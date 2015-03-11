package com;

import java.util.List;

public class AggrHelper {

	/**
	 * This Class contains functions to help in calculating the values of Aggregate Functions, Min, Max, Avg, Sum
	 */
	static int aggrfunc_findmin(List<Integer> cur)
	{
		// TODO Auto-generated method stub
		int i,min=100000000;
		for(i=0;i<cur.size();i++)
		{
			if(cur.get(i)<min)
				min=cur.get(i);
			
		}
		return min;
	}
	 static int aggrfunc_findmax(List<Integer> cur)
	{
		// TODO Auto-generated method stub
		int i,max=-1;
		for(i=0;i<cur.size();i++)
		{
			if(cur.get(i)>max)
				max=cur.get(i);
			
		}
		return max;
	}
	 static int aggrfunc_findsum(List<Integer> cur)
	{
		// TODO Auto-generated method stub
		int i,sum=0;
		for(i=0;i<cur.size();i++)
		{
			sum=sum+cur.get(i);
		}
		return sum;
	}
	 static int aggrfunc_findavg(List<Integer> cur)
	{
		// TODO Auto-generated method stub
		int i,sum=0;
		for(i=0;i<cur.size();i++)
		{
			sum=sum+cur.get(i);
		}
		return sum/cur.size();
	}

}
