package com.salesforce.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

public class Database {
	
	public static final String dbClassName= "com.mysql.cj.jdbc.Driver";
    Connection myCon = null;
    Statement mySt,mySt1;
    PreparedStatement myPst,myPst1;
    ResultSet rs1,rs2,rs3,rs4;
    String myQuery1,myQuery2;
    String mySQL,myUName,myPswd;
	
	public Database() {
					
		 	mySQL  	= "jdbc:mysql://40.122.188.55:3306/pgtest_marina2?autoReconnect=true&useSSL=false";
	    	myUName = "adnan_sharaf";
	    	myPswd  =  "MLM@Sharaf_@dnan!";
	    	try {
				Class.forName(dbClassName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	    	try {
				myCon = DriverManager.getConnection(mySQL,myUName,myPswd);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
	}
	
	
	public void deleteRateGroup(String rateGroupName) {
		
		checkDBConnection();
		
		try {
		
			myQuery1 = "select count(*) as count From pgtest_marina2.price_groups where name = ? ;";
	        myPst = myCon.prepareStatement(myQuery1);
	        myPst.setString(1, rateGroupName);
	        rs1 = myPst.executeQuery();
	        rs1.next();
	        
			int count = rs1.getInt("count");
			if(count > 0) {
			
				 myQuery1 = "select id From pgtest_marina2.price_groups where name = ? ;";
		         myPst = myCon.prepareStatement(myQuery1);
		         myPst.setString(1, rateGroupName);
		         rs1 = myPst.executeQuery();
		         rs1.next();
		         
		         int group_id = rs1.getInt("id");
		        
				 mySt = myCon.createStatement ();
				 myQuery1 = "delete from pgtest_marina2.pricing_daily where group_id = ? ;";
				 myPst = myCon.prepareStatement (myQuery1);
				 myPst.setInt(1, group_id);
				 myPst.executeUpdate();
				 
				 mySt = myCon.createStatement ();
				 myQuery1 = "delete from pgtest_marina2.pricing_monthly where group_id = ? ;";
				 myPst = myCon.prepareStatement (myQuery1);
				 myPst.setInt(1, group_id);
				 myPst.executeUpdate();
				 
				 mySt = myCon.createStatement ();
				 myQuery1 = "delete from pgtest_marina2.pricing_annual where group_id = ? ;";
				 myPst = myCon.prepareStatement (myQuery1);
				 myPst.setInt(1, group_id);
				 myPst.executeUpdate();
				 
				 mySt = myCon.createStatement ();
				 myQuery1 = "delete from pgtest_marina2.price_groups where id = ? ;";
				 myPst = myCon.prepareStatement (myQuery1);
				 myPst.setInt(1, group_id);
				 myPst.executeUpdate();
				
			}
			
			
		}catch(SQLException sqe) {}
		
        
		
	}
	
	public void deleteReservation(String slipName) {
		
		checkDBConnection();
		
		try {
	
			myQuery1 = "select count(*) as count From pgtest_marina2.inventory_item where slip_id = ? and deleted_by is null ;";
	        myPst = myCon.prepareStatement(myQuery1);
	        myPst.setString(1, slipName);
	        rs1 = myPst.executeQuery();
	        rs1.next();
	        
	        int count = rs1.getInt("count");
	        
	        if(count > 0) {
	        	
	        	myQuery1 = "select * From pgtest_marina2.inventory_item where slip_id = ? and deleted_by is null ;";
		        myPst = myCon.prepareStatement(myQuery1);
		        myPst.setString(1, slipName);
		        rs1 = myPst.executeQuery();
		        rs1.next();
		        
		        String inventory_id = rs1.getString("inventory_id");
		        
		        myQuery1 = "select count(*) as count From pgtest_marina2.reservations where slip_id = ? ;";
		        myPst = myCon.prepareStatement(myQuery1);
		        myPst.setString(1, inventory_id);
		        rs1 = myPst.executeQuery();
		        rs1.next();
		        
		        int count_reservation = rs1.getInt("count");
		        
		        if(count_reservation > 0) {
		        	
		        	    myQuery1 = "select * From pgtest_marina2.reservations where slip_id = ? ;";
				        myPst = myCon.prepareStatement(myQuery1);
				        myPst.setString(1, inventory_id);
				        rs1 = myPst.executeQuery();
				        rs1.next();
				        
				        int reservation_id = rs1.getInt("id");
				        
				         mySt = myCon.createStatement ();
						 myQuery1 = "delete from pgtest_marina2.reservations where id = ? ;";
						 myPst = myCon.prepareStatement (myQuery1);
						 myPst.setInt(1, reservation_id);
						 myPst.executeUpdate();
						 
						 mySt = myCon.createStatement ();
						 myQuery1 = "delete from pgtest_marina2.orders where reservation_id = ? ;";
						 myPst = myCon.prepareStatement (myQuery1);
						 myPst.setInt(1, reservation_id);
						 myPst.executeUpdate();
		        	
		        }
		        
	        	
	        }
	        
	        myCon.close();
	        
		}catch(SQLException sql) {
			
			
		}
	
		
	}
	
	public double getPricingYearly(String rateGroupName, String boatLength, String startDate, String endDate, String oneTimePay) throws ParseException {
		
		DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd");
		Date sdate = userDateFormat.parse(startDate);
		startDate = dateFormatNeeded.format(sdate);
		Date edate = userDateFormat.parse(endDate);
		endDate = dateFormatNeeded.format(edate);
		
		checkDBConnection();
		double totalRate = 0, yearRate, monthRate = 0, dayRate = 0;
		try {
			
			myQuery1 = "select count(*) as count From pgtest_marina2.price_groups where name = ? ;";
	        myPst = myCon.prepareStatement(myQuery1);
	        myPst.setString(1, rateGroupName);
	        rs1 = myPst.executeQuery();
	        rs1.next();
	        
	        int count = rs1.getInt("count");
	        if(count > 0) {
	        	
	        	myQuery1 = "select id From pgtest_marina2.price_groups where name = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setString(1, rateGroupName);
	            rs1 = myPst.executeQuery();
	            rs1.next();
	            
	            long groupID = rs1.getLong("id");
	            
	            long bucket_id = 0;
	            int index = 0, counter = 0;
	            
	            myQuery1 = "select count(*) as count from pgtest_marina2.boat_buckets where type = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setLong(1, 3);
	            rs1 = myPst.executeQuery();
	            rs1.next();
	            
	            count = rs1.getInt("count");
	            
	            myQuery1 = "select * from pgtest_marina2.boat_buckets where type = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setLong(1, 3);
	            rs1 = myPst.executeQuery();
	            while(rs1.next()) {
	            	
	            	counter++;
	            	float maxVal = rs1.getFloat("max");
	            	
	            	if(Float.parseFloat(boatLength) < maxVal) {
	            		bucket_id = rs1.getLong("id");
	            		break;
	            	}else {
	            		if(counter == count) {
	            			bucket_id = rs1.getLong("id");
	            			break;
	            		}
	            	}
	            	
	            	index++;
	            }
	            
	            
	            if(oneTimePay.equalsIgnoreCase("yes")) {
	            	
	            	String[] parts = startDate.split("-");
	            	int startYear = Integer.parseInt(parts[0]);
	            	int startMonth = Integer.parseInt(parts[1]);
	            	int startDay = Integer.parseInt(parts[2]);
	            	
	            	parts = endDate.split("-");
	            	int endYear = Integer.parseInt(parts[0]);
	            	int endMonth = Integer.parseInt(parts[1]);
	            	int endDay = Integer.parseInt(parts[2]);
	            	
	            	LocalDate start = LocalDate.of(startYear,startMonth,startDay);
					LocalDate end = LocalDate.of(endYear,endMonth,endDay);
				
					Period diff = Period.between(start, end); 
					
					int noOfYears = diff.getYears();
					int noOfMonths = diff.getMonths();
					int noOfDays = diff.getDays();
					
					myQuery1 = "select * from pricing_annual where group_id = ? and bucket_id = ? order by effective desc ;";
		            myPst = myCon.prepareStatement(myQuery1);
		            myPst.setLong(1, groupID);
		            myPst.setLong(2, bucket_id);
		            rs1 = myPst.executeQuery();
		            rs1.next();
	            	
		            double price = rs1.getDouble("price");
					double discount = rs1.getDouble("one_time_discount");
					
					price = price / 100;
					discount = discount / 100;
					double totalPercentage =  (discount + 100) / 100;
				
				//Get the rate which is applied per month
					double ini_yearRatePerMonth = price / totalPercentage;
					ini_yearRatePerMonth = Utilities.roundValue(ini_yearRatePerMonth, 2);
					yearRate = ini_yearRatePerMonth;
					
					yearRate = yearRate * noOfYears * Integer.parseInt(boatLength) * 12;
					yearRate = Utilities.roundValue(yearRate, 2);
					
				//If No Of Months are greater than 0	
					if(noOfMonths > 0) {
						
					    monthRate = ini_yearRatePerMonth;
						monthRate = monthRate * noOfMonths * Integer.parseInt(boatLength);
						monthRate = Utilities.roundValue(monthRate, 2);
						
					}
					
				//If No of Days are greater than 0	
					if(noOfDays > 0) {
						
					//Add No of Days and No of Years to the start Date and get One date	
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						Calendar c = Calendar.getInstance();
						c.setTime(formatter.parse(startDate));

						c.add(Calendar.MONTH, noOfMonths);
						c.add(Calendar.YEAR, noOfYears);
						String calculatedDateAfterYearMonth = formatter.format(c.getTimeInMillis());
						
						String[] parts1 = calculatedDateAfterYearMonth.split("-");
						int yearCalc = Integer.parseInt(parts1[0]);
						int monthCalc = Integer.parseInt(parts1[1]);
						int dayCalc = Integer.parseInt(parts1[2]);
				
				//Calculate the No of Days of the landing month after adding the years and months on start date		
						YearMonth yearMonthObject = YearMonth.of(yearCalc, monthCalc);
						int daysInMonth = yearMonthObject.lengthOfMonth();
				
			   //Check if Landing Month is equal to End Date month, so that we dont have to segregate the per day rate logic separately as 
			   // End month and landing month are same
						if(monthCalc == endMonth) {
							
							dayRate = ini_yearRatePerMonth / daysInMonth;
							dayRate = Utilities.roundValue(dayRate, 2);
							dayRate = dayRate * Integer.parseInt(boatLength) * noOfDays;
							dayRate = Utilities.roundValue(dayRate, 2);
							
						}else {
							
				//If Landing Month and End Date month are different, then first subract get the landing month no of days and then  
				//subract it with landing month day, it will give the count of days that we need to include in charges for that month 
							
							
							double dayRate1 = ini_yearRatePerMonth / daysInMonth;
							dayRate1 = Utilities.roundValue(dayRate1, 2);
					//Here dayCalc is landing month day and subract it with total number of days to get the charge day count in calcDaysInMonth		
							int calcDaysInMonth = daysInMonth - dayCalc;
							
							dayRate1 = dayRate1 * calcDaysInMonth * Integer.parseInt(boatLength);
							dayRate1 = Utilities.roundValue(dayRate1, 2);
							
				//Now Add the Landing date which is calculatedDateAfterYearMonth with CalcDaysInMonth that are charged days of that month 
				//to get the second month charges of specific days, date goes to calculatedDateAfterYearNextMonth
							
							c = Calendar.getInstance();
							c.setTime(formatter.parse(calculatedDateAfterYearMonth));
							c.add(Calendar.DAY_OF_MONTH, calcDaysInMonth);
							String calculatedDateAfterYearNextMonth = formatter.format(c.getTimeInMillis());
							parts1 = calculatedDateAfterYearNextMonth.split("-");
							
							int yearCalc1 = Integer.parseInt(parts1[0]);
							int monthCalc1 = Integer.parseInt(parts1[1]);
							int dayCalc1 = Integer.parseInt(parts1[2]);

							yearMonthObject = YearMonth.of(yearCalc1, monthCalc1);
							daysInMonth = yearMonthObject.lengthOfMonth();
							//String[] parts1 = calculatedDateAfterYearMonth
							LocalDate startDiff = LocalDate.of(yearCalc1,monthCalc1,dayCalc1);
							LocalDate endDiff = LocalDate.of(endYear,endMonth,endDay);
					
					//Get the difference of Enddate and last landing date of next month to get the last line item charges		
							diff = Period.between(startDiff, endDiff); 
							int daysDifference = diff.getDays();
							
							double dayRate2 = ini_yearRatePerMonth / daysInMonth;
							dayRate2 = Utilities.roundValue(dayRate2, 2);
							dayRate2 = dayRate2 * daysDifference * Integer.parseInt(boatLength);
							dayRate2 = Utilities.roundValue(dayRate2, 2);
							
							dayRate = dayRate1 + dayRate2;
							
						}
						
						
						
					}
	            	
					totalRate = yearRate + monthRate + dayRate;
					
	            }else {
	            	
	            	
	            }
	            
	        	
	        }
			
		}catch(SQLException sqe) {}
		
		return totalRate;
	}
	
	
	public double getPricingMonthly_Day(String rateGroupName, String boatLength, int startYear, 
			int startMonth, int startDay, int endYear, int endMonth, int endDay, String oneTimePay, String isDaysCalculated) {

		checkDBConnection();
		double monthRate = 0;
		try {
			myQuery1 = "select count(*) as count From pgtest_marina2.price_groups where name = ? ;";
	        myPst = myCon.prepareStatement(myQuery1);
	        myPst.setString(1, rateGroupName);
	        rs1 = myPst.executeQuery();
	        rs1.next();
	        
	        int count = rs1.getInt("count");
	        if(count > 0) {
	        	
	        	myQuery1 = "select id From pgtest_marina2.price_groups where name = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setString(1, rateGroupName);
	            rs1 = myPst.executeQuery();
	            rs1.next();
	            
	            long groupID = rs1.getLong("id");
	            
	            long bucket_id = 0;
	            int index = 0, counter = 0;
	            
	            myQuery1 = "select count(*) as count from pgtest_marina2.boat_buckets where type = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setLong(1, 2);
	            rs1 = myPst.executeQuery();
	            rs1.next();
	            
	            count = rs1.getInt("count");
	            
	            myQuery1 = "select * from pgtest_marina2.boat_buckets where type = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setLong(1, 2);
	            rs1 = myPst.executeQuery();
	            while(rs1.next()) {
	            	
	            	counter++;
	            	float maxVal = rs1.getFloat("max");
	            	
	            	if(Float.parseFloat(boatLength) < maxVal) {
	            		bucket_id = rs1.getLong("id");
	            		break;
	            	}else {
	            		if(counter == count) {
	            			bucket_id = rs1.getLong("id");
	            			break;
	            		}
	            	}
	            	
	            	index++;
	            }
	            
	            LocalDate start = LocalDate.of(startYear,startMonth,startDay);
				LocalDate end = LocalDate.of(endYear,endMonth,endDay);
				
				Period diff = Period.between(start, end);
				int noOfDaysCalc = diff.getDays();
				
				int checkCond = 0 ;
				counter = 0;
				String yearSelected = "";

				while (!start.equals(end)) {
					//2023-12-03
					String startDateIndvidual = start.toString();
					
					String[] parts = startDateIndvidual.split("-");
					String year = parts[0];
					String month = parts[1];
					String day = parts[2];
					month = month.replaceFirst("^0+(?!$)", "");
					
					int startMonthDB = 0, endMonthDB = 0;
					
					while(checkCond == 0) {
				
						if(counter == 0)
							yearSelected = year;
						
						myQuery1 = "select count(*) as count from pricing_monthly where group_id = ? and year = ? and bucket_id = ?;";
						myPst = myCon.prepareStatement(myQuery1);
						myPst.setLong(1, groupID);
						myPst.setInt(2, Integer.parseInt(yearSelected));
						myPst.setLong(3, bucket_id);
						rs1 = myPst.executeQuery();
						rs1.next();
						
						int count_rec = rs1.getInt("count");
						
						if(count_rec == 0) {
							
							int yearI = Integer.parseInt(yearSelected);
							yearI = Integer.parseInt(yearSelected) - 1;
							yearSelected = Integer.toString(yearI);
							
						}else {
							
							checkCond = 1;
						}
						
						counter++;
						
					}
					
					
					myQuery1 = "select * from pricing_monthly where group_id = ? and year = ? and bucket_id = ?;";
					myPst = myCon.prepareStatement(myQuery1);
					myPst.setLong(1, groupID);
					myPst.setInt(2, Integer.parseInt(yearSelected));
					myPst.setLong(3, bucket_id);
					rs1 = myPst.executeQuery();
					while(rs1.next()) {
						
						int start_month = rs1.getInt("start_month");
						int end_month = rs1.getInt("end_month");
						
						if(Integer.parseInt(month) >= start_month && Integer.parseInt(month) <= end_month) {
							
							startMonthDB = start_month;
							endMonthDB = end_month;
							break;
						}
					}
					
					
					myQuery1 = "select * from pricing_monthly where group_id = ? and year = ? and bucket_id = ? and start_month = ? "
							+ "and end_month = ? order by id desc;";
					myPst = myCon.prepareStatement(myQuery1);
					myPst.setLong(1, groupID);
					myPst.setInt(2, Integer.parseInt(yearSelected));
					myPst.setLong(3, bucket_id);
					myPst.setInt(4, startMonthDB);
					myPst.setInt(5, endMonthDB);
					rs1 = myPst.executeQuery();
					rs1.next();
	            
					double ini_monthRate = 0;
					if(oneTimePay.equalsIgnoreCase("yes")) {
						
						if(isDaysCalculated.equalsIgnoreCase("yes")) {
							
							YearMonth yearMonthObject = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
							int daysInMonth = yearMonthObject.lengthOfMonth();
							
							double price = rs1.getDouble("price");
							double discount = rs1.getDouble("one_time_discount");
							
							price = price / 100;
							discount = discount / 100;
							double totalPercentage =  (discount + 100) / 100;
							
							ini_monthRate = price / totalPercentage;
							ini_monthRate = ini_monthRate / daysInMonth;
							ini_monthRate = Utilities.roundValue(ini_monthRate, 2);
							
							if(Integer.parseInt(month) != endMonth) {
								
								LocalDate monthDateStart = LocalDate.of(startYear,startMonth,startDay-1);
								LocalDate endDayOfMonth = monthDateStart.with(lastDayOfMonth());
								
								diff = Period.between(monthDateStart, endDayOfMonth);
								int noOfDaysCalcPerMonth = diff.getDays();
								
								ini_monthRate = ini_monthRate * noOfDaysCalcPerMonth;
								ini_monthRate = Utilities.roundValue(ini_monthRate, 2);
								
							}else {
								
								LocalDate monthDateStart = LocalDate.of(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
								monthDateStart = monthDateStart.with(firstDayOfMonth());
								
								diff = Period.between(monthDateStart, end);
								int noOfDaysCalcPerMonth = diff.getDays();
								
								ini_monthRate = ini_monthRate * noOfDaysCalcPerMonth;
								ini_monthRate = Utilities.roundValue(ini_monthRate, 2);
								
							}
							
						}else {
						
							double price = rs1.getDouble("price");
							double discount = rs1.getDouble("one_time_discount");
							
							price = price / 100;
							discount = discount / 100;
							double totalPercentage =  (discount + 100) / 100;
							
							ini_monthRate = price / totalPercentage;

						}
						
												
					}else {
						
						if(isDaysCalculated.equalsIgnoreCase("yes")) {
							
							YearMonth yearMonthObject = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
							int daysInMonth = yearMonthObject.lengthOfMonth();
							
							ini_monthRate = rs1.getFloat("price");
							ini_monthRate = ini_monthRate / 100;
							ini_monthRate = ini_monthRate / daysInMonth;
							ini_monthRate = Utilities.roundValue(ini_monthRate, 2);
							
							if(Integer.parseInt(month) != endMonth) {
							
								LocalDate monthDateStart = LocalDate.of(startYear,startMonth,startDay);
								LocalDate endDayOfMonth = monthDateStart.with(lastDayOfMonth());
								
								diff = Period.between(monthDateStart, endDayOfMonth);
								int noOfDaysCalcPerMonth = diff.getDays();
								
								ini_monthRate = ini_monthRate * noOfDaysCalcPerMonth;
								
							}else {
								
								LocalDate monthDateStart = LocalDate.of(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
								monthDateStart = monthDateStart.with(firstDayOfMonth());
								
								diff = Period.between(monthDateStart, end);
								int noOfDaysCalcPerMonth = diff.getDays();
								
								ini_monthRate = ini_monthRate * noOfDaysCalcPerMonth;
								
							}
							
							
						}else {
						
							ini_monthRate = rs1.getFloat("price");
							ini_monthRate = ini_monthRate / 100;
							
						}
						
						
						
					}
					if(monthRate == 0)
						monthRate = ini_monthRate;
					else 
						monthRate = monthRate + ini_monthRate;
					
					System.out.println(start.toString());
					if(start.getMonth() == end.getMonth())
						break;
					else
					 start = start.plusMonths(1);
					 
				}
	           
	            
	        }
			
	        myCon.close();
	        
		}catch(SQLException sqe) {}
		
		return monthRate;
		
	
	}
	
	public double getPricingMonthly(String rateGroupName, String boatLength, int startYear, 
			int startMonth, int startDay, int endYear, int endMonth, int endDay, String oneTimePay) {

		checkDBConnection();
		double monthRate = 0;
		try {
			myQuery1 = "select count(*) as count From pgtest_marina2.price_groups where name = ? ;";
	        myPst = myCon.prepareStatement(myQuery1);
	        myPst.setString(1, rateGroupName);
	        rs1 = myPst.executeQuery();
	        rs1.next();
	        
	        int count = rs1.getInt("count");
	        if(count > 0) {
	        	
	        	myQuery1 = "select id From pgtest_marina2.price_groups where name = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setString(1, rateGroupName);
	            rs1 = myPst.executeQuery();
	            rs1.next();
	            
	            long groupID = rs1.getLong("id");
	            
	            long bucket_id = 0;
	            int index = 0, counter = 0;
	            
	            myQuery1 = "select count(*) as count from pgtest_marina2.boat_buckets where type = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setLong(1, 2);
	            rs1 = myPst.executeQuery();
	            rs1.next();
	            
	            count = rs1.getInt("count");
	            
	            myQuery1 = "select * from pgtest_marina2.boat_buckets where type = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setLong(1, 2);
	            rs1 = myPst.executeQuery();
	            while(rs1.next()) {
	            	
	            	counter++;
	            	float maxVal = rs1.getFloat("max");
	            	
	            	if(Float.parseFloat(boatLength) < maxVal) {
	            		bucket_id = rs1.getLong("id");
	            		break;
	            	}else {
	            		if(counter == count) {
	            			bucket_id = rs1.getLong("id");
	            			break;
	            		}
	            	}
	            	
	            	index++;
	            }
	            
	            LocalDate start = LocalDate.of(startYear,startMonth,startDay);
				LocalDate end = LocalDate.of(endYear,endMonth,endDay);
				
				int checkCond = 0 ;
				counter = 0;
				String yearSelected = "";

				while (!start.equals(end)) {
					//2023-12-03
					String startDateIndvidual = start.toString();
					
					String[] parts = startDateIndvidual.split("-");
					String year = parts[0];
					String month = parts[1];
					//String day = parts[2];
					month = month.replaceFirst("^0+(?!$)", "");
					
					int startMonthDB = 0, endMonthDB = 0;
					
					while(checkCond == 0) {
				
						if(counter == 0)
							yearSelected = year;
						
						myQuery1 = "select count(*) as count from pricing_monthly where group_id = ? and year = ? and bucket_id = ?;";
						myPst = myCon.prepareStatement(myQuery1);
						myPst.setLong(1, groupID);
						myPst.setInt(2, Integer.parseInt(yearSelected));
						myPst.setLong(3, bucket_id);
						rs1 = myPst.executeQuery();
						rs1.next();
						
						int count_rec = rs1.getInt("count");
						
						if(count_rec == 0) {
							
							int yearI = Integer.parseInt(yearSelected);
							yearI = Integer.parseInt(yearSelected) - 1;
							yearSelected = Integer.toString(yearI);
							
						}else {
							
							checkCond = 1;
						}
						
						counter++;
						
					}
					
					
					myQuery1 = "select * from pricing_monthly where group_id = ? and year = ? and bucket_id = ?;";
					myPst = myCon.prepareStatement(myQuery1);
					myPst.setLong(1, groupID);
					myPst.setInt(2, Integer.parseInt(yearSelected));
					myPst.setLong(3, bucket_id);
					rs1 = myPst.executeQuery();
					while(rs1.next()) {
						
						int start_month = rs1.getInt("start_month");
						int end_month = rs1.getInt("end_month");
						
						if(Integer.parseInt(month) >= start_month && Integer.parseInt(month) <= end_month) {
							
							startMonthDB = start_month;
							endMonthDB = end_month;
							break;
						}
					}
					
					
					myQuery1 = "select * from pricing_monthly where group_id = ? and year = ? and bucket_id = ? and start_month = ? "
							+ "and end_month = ? order by id desc;";
					myPst = myCon.prepareStatement(myQuery1);
					myPst.setLong(1, groupID);
					myPst.setInt(2, Integer.parseInt(yearSelected));
					myPst.setLong(3, bucket_id);
					myPst.setInt(4, startMonthDB);
					myPst.setInt(5, endMonthDB);
					rs1 = myPst.executeQuery();
					rs1.next();
	            
					double ini_monthRate;
					if(oneTimePay.equalsIgnoreCase("yes")) {
						
						double price = rs1.getDouble("price");
						double discount = rs1.getDouble("one_time_discount");
						
						price = price / 100;
						discount = discount / 100;
						double totalPercentage =  (discount + 100) / 100;
						
						ini_monthRate = price / totalPercentage;
						
					}else {
						
						ini_monthRate = rs1.getFloat("price");
						ini_monthRate = ini_monthRate / 100f;
						
					}
					if(monthRate == 0)
						monthRate = ini_monthRate;
					else 
						monthRate = monthRate + ini_monthRate;
					
					System.out.println(start.toString());
					 start = start.plusMonths(1);
					 
				}
	           
	            
	        }
			
	        myCon.close();
	        
		}catch(SQLException sqe) {}
		
		return monthRate;
		
	}
	
	public double getPricingDaily(String rateGroupName, String boatLength, int startYear, 
			int startMonth, int startDay, int endYear, int endMonth, 
			int endDay, int newBucket) {

		checkDBConnection();
		double dayRate = 0;
		try {
			myQuery1 = "select count(*) as count From pgtest_marina2.price_groups where name = ? ;";
	        myPst = myCon.prepareStatement(myQuery1);
	        myPst.setString(1, rateGroupName);
	        rs1 = myPst.executeQuery();
	        rs1.next();
	        
	        int count = rs1.getInt("count");
	        if(count > 0) {
	        	
	        	myQuery1 = "select id From pgtest_marina2.price_groups where name = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setString(1, rateGroupName);
	            rs1 = myPst.executeQuery();
	            rs1.next();
	            
	            long groupID = rs1.getLong("id");
	            
	            long bucket_id = 0;
	            int index = 0, counter = 0;
	            
	            myQuery1 = "select count(*) as count from pgtest_marina2.boat_buckets where type = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setLong(1, 1);
	            rs1 = myPst.executeQuery();
	            rs1.next();
	            
	            count = rs1.getInt("count");
	            
	            myQuery1 = "select * from pgtest_marina2.boat_buckets where type = ? ;";
	            myPst = myCon.prepareStatement(myQuery1);
	            myPst.setLong(1, 1);
	            rs1 = myPst.executeQuery();
	            while(rs1.next()) {
	            	
	            	counter++;
	            	float maxVal = rs1.getFloat("max");
	            	
	            	if(Float.parseFloat(boatLength) < maxVal) {
	            		bucket_id = rs1.getLong("id");
	            		break;
	            	}else {
	            		if(counter == count) {
	            			bucket_id = rs1.getLong("id");
	            			break;
	            		}
	            	}
	            	
	            	index++;
	            }
	            
	            LocalDate start = LocalDate.of(startYear,startMonth,startDay);
				LocalDate end = LocalDate.of(endYear,endMonth,endDay);
				int checkCond = 0;
				counter = 0;
				String yearSelected = "";
				
				while (!start.equals(end)) {
					//2023-12-03
					String startDateIndvidual = start.toString();
					
					String[] parts = startDateIndvidual.split("-");
					String year = parts[0];
					String month = parts[1];
					String day = parts[2];
					day = day.replaceFirst("^0+(?!$)", "");
					month = month.replaceFirst("^0+(?!$)", "");
					
					
					while(checkCond == 0) {
						
						if(counter == 0)
							yearSelected = year;
						
						myQuery1 = "select count(*) as count from pricing_daily where group_id = ? and year = ? and month = ? and bucket_id = ? order by id desc;";
						myPst = myCon.prepareStatement(myQuery1);
						myPst.setLong(1, groupID);
						myPst.setInt(2, Integer.parseInt(yearSelected));
						myPst.setInt(3, Integer.parseInt(month));
						myPst.setLong(4, bucket_id);
						rs1 = myPst.executeQuery();
						rs1.next();
						
						int count_rec = rs1.getInt("count");
						if(count_rec == 0) {

							int yearI = Integer.parseInt(yearSelected);
							yearI = Integer.parseInt(yearSelected) - 1;
							yearSelected = Integer.toString(yearI);
							
						}else {
							
							//yearSelected = year;
							checkCond = 1;
						}
							
							counter++;
					}
					myQuery1 = "select * from pricing_daily where group_id = ? and year = ? and month = ? and bucket_id = ? order by id desc;";
					myPst = myCon.prepareStatement(myQuery1);
					myPst.setLong(1, groupID);
					myPst.setInt(2, Integer.parseInt(yearSelected));
					myPst.setInt(3, Integer.parseInt(month));
					myPst.setLong(4, bucket_id);
					rs1 = myPst.executeQuery();
					rs1.next();
					
					//int ini_dayRate = Double.valurs1.getInt("day_"+day);
					double ini_dayRate = Double.valueOf(rs1.getInt("day_"+day));
					ini_dayRate = ini_dayRate / 100;
					
					if(dayRate == 0)
						dayRate = ini_dayRate;
					else 
						dayRate = dayRate + ini_dayRate;
					
					System.out.println(start.toString());
					 start = start.plusDays(1);
					 
				}
	           
	            
	        }
			
	        myCon.close();
	        
		}catch(SQLException sqe) {}
		
		
		
		return dayRate;
		
	}
		
	public void checkDBConnection() {
		try {
		
			myQuery1 = "select * From pgtest_marina2.login_tokens limit 1 ;";
	        myPst = myCon.prepareStatement(myQuery1);
	        rs1 = myPst.executeQuery();
	        rs1.next();
	        
		}catch(SQLException sqe) {
			
			mySQL  	= "jdbc:mysql://40.122.188.55:3306/pgtest_marina2?autoReconnect=true&useSSL=false";
	    	myUName = "adnan_sharaf";
	    	myPswd  =  "MLM@Sharaf_@dnan!";
	    	try {
				Class.forName(dbClassName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	    	try {
				myCon = DriverManager.getConnection(mySQL,myUName,myPswd);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
		
	}
	


}
