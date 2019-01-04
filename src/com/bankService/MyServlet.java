package com.bankService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;
import com.moneymoney.exception.InvalidInputException;


@WebServlet("*.mm")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher;
	private boolean order = true;
 
    public MyServlet() {
        super();
       
    }
    @Override
    public void init() throws ServletException {
    	super.init();
    	try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection       //connection created
					("jdbc:mysql://localhost:3306/bankapp_db", "root", "root");
			PreparedStatement preparedStatement = 
					connection.prepareStatement("DELETE FROM ACCOUNT");
			preparedStatement.execute();
		} catch (ClassNotFoundException| SQLException e) {
			e.printStackTrace();
		}
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SavingsAccountService savingsAccountService = new SavingsAccountServiceImpl();
		PrintWriter out = response.getWriter();
		String path = request.getServletPath();
		switch (path) {
		case "/addNewAccount.mm":
			response.sendRedirect("AddNewAccount.jsp");     //transfer to AddNewAccount.html
			break;
			
		case "/newAccount.mm":
			String accountHolderName = request.getParameter("Enter_name");
			double accountBalance = Double.parseDouble(request.getParameter("Enter_Balance"));
			boolean salarytype = request.getParameter("type").equalsIgnoreCase("y")?true:false;
			try {
				savingsAccountService.createNewAccount(accountHolderName,accountBalance, salarytype);
				response.sendRedirect("getAll.mm");
			} catch (ClassNotFoundException| SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case "/closeAccount.mm":
			response.sendRedirect("closeAccount.jsp");
			break;
			
		case "/closedAccount.mm":                 // closed account operation
			int accountNumber = Integer.parseInt(request.getParameter("Enter_Account_No"));
			try {
				savingsAccountService.deleteAccount(accountNumber);
				DBUtil.commit();
			} catch (ClassNotFoundException| InvalidInputException| SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case "/currentBalance.mm":
			response.sendRedirect("GetCurrentBalanace.jsp");
			break;
			
		case "/checkBalance.mm":                         // check Balance operation
			accountNumber = Integer.parseInt(request.getParameter("Enter_Account_No"));
			try {
				savingsAccountService.checkAccountBalance(accountNumber);
				out.println(savingsAccountService.checkAccountBalance(accountNumber));
			} catch (ClassNotFoundException| InvalidInputException| SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case "/withdrawAmount.mm":
			response.sendRedirect("withdraw.jsp");
			break;
			
		case "/amountwithdraw.mm":
			accountNumber = Integer.parseInt(request.getParameter("Enter_Account_No"));
			double amount = Double.parseDouble(request.getParameter("Enter_Amount"));
			SavingsAccount savingsAccount; 
			try {
				savingsAccount = savingsAccountService.getAccountById(accountNumber);
				savingsAccountService.withdraw(savingsAccount, amount);
				DBUtil.commit();
			} catch (ClassNotFoundException| InvalidInputException| SQLException| AccountNotFoundException e) {
				e.printStackTrace();
			}
			break;
			
		case "/depositAmount.mm":
			response.sendRedirect("deposit.jsp");
			break;
			
		case "/amountdeposit.mm":
			accountNumber = Integer.parseInt(request.getParameter("Enter_Account_No"));
			amount = Double.parseDouble(request.getParameter("Enter_Amount"));
			
			try {
				savingsAccount = savingsAccountService.getAccountById(accountNumber);
				savingsAccountService.deposit(savingsAccount, amount);
				DBUtil.commit();
			} catch (ClassNotFoundException| InvalidInputException| SQLException| AccountNotFoundException e) {
				e.printStackTrace();
			}
			break;
			
		case "/transferFund.mm":
			response.sendRedirect("fundTransfer.jsp");
			break;
			
			
			case "/fundTransfer.mm":
				int senderAccountNumber = Integer.parseInt(request.getParameter("Enter_Sender_Account_No"));
				int receiverAccountNumber = Integer.parseInt(request.getParameter("Enter_Reciever_Account_No"));
				amount = Double.parseDouble(request.getParameter("Enter_Amount"));
				
				try {
					SavingsAccount senderSavingsAccount = savingsAccountService.getAccountById(senderAccountNumber);
					SavingsAccount receiverSavingsAccount = savingsAccountService.getAccountById(receiverAccountNumber);
					savingsAccountService.fundTransfer(senderSavingsAccount, receiverSavingsAccount, amount);
					out.println("fundTransfer execute Successfully");
					DBUtil.commit();
				} catch (ClassNotFoundException| InvalidInputException| SQLException| AccountNotFoundException e) {
					
					e.printStackTrace();
				} 
				break;
				
			case "/searchForm.mm":
				response.sendRedirect("SearchForm.jsp");
				break;
			case "/search.mm":
				accountNumber = Integer.parseInt(request.getParameter("txtAccountNumber"));
				try {
					SavingsAccount account = savingsAccountService.getAccountById(accountNumber);
					request.setAttribute("account", account);
					dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
					dispatcher.forward(request, response);
				} catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
					e.printStackTrace();
				}
				break;
				
			case "/getAll.mm":
				try {
					List<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
					request.setAttribute("accounts", accounts);
					dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
					dispatcher.forward(request, response);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;
				
			case "/sortByName.mm":
				try {
					Collection<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
					Set<SavingsAccount> accountSet = null;
					if(order){
						accountSet = new TreeSet<>(new Comparator<SavingsAccount>() {
						@Override
						public int compare(SavingsAccount arg0, SavingsAccount arg1) {
							return arg0.getBankAccount().getAccountHolderName().compareTo
									(arg1.getBankAccount().getAccountHolderName());
						}
					}
					);
					order = false;
					}
					else
					{
						accountSet = new TreeSet<>(new Comparator<SavingsAccount>() {
							@Override
							public int compare(SavingsAccount arg0, SavingsAccount arg1) {
								return arg1.getBankAccount().getAccountHolderName().compareTo
										(arg0.getBankAccount().getAccountHolderName());
							}
						}
						);
						order = true;
					}
					accountSet.addAll(accounts);
					request.setAttribute("accounts", accountSet);
					dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
					dispatcher.forward(request, response);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case "/sortByBalance.mm":
				try {
					Collection<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
					Set<SavingsAccount> accountSet = null;
					if(order){
						accountSet = new TreeSet<>(new Comparator<SavingsAccount>() {
						@Override
						public int compare(SavingsAccount arg0, SavingsAccount arg1) {
							return Double.compare(arg0.getBankAccount().getAccountBalance(),arg1.getBankAccount().
									getAccountBalance());
						}
					}
					);
					order = false;
					}
					else
					{
						accountSet = new TreeSet<>(new Comparator<SavingsAccount>() {
							@Override
							public int compare(SavingsAccount arg0, SavingsAccount arg1) {
								return Double.compare(arg1.getBankAccount().getAccountBalance(),arg0.getBankAccount().
										getAccountBalance());
							}
						}
						);
						order = true;
					}
					accountSet.addAll(accounts);
					request.setAttribute("accounts", accountSet);
					dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
					dispatcher.forward(request, response);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case "/update.mm":
				response.sendRedirect("updateACC.jsp");
				break;
			case "/updateaccountform.mm":
			try {
				SavingsAccount account = savingsAccountService.getAccountById(Integer.parseInt(request.getParameter("Enter_Account_No")));
				request.setAttribute("account", account);
				dispatcher = request.getRequestDispatcher("updateAccount.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e1) {
				e1.printStackTrace();
			}
				break;
			case "/updateAccount.mm":
				accountNumber = Integer.parseInt(request.getParameter("accountNumber"));
			try {
				savingsAccount = savingsAccountService.getAccountById(accountNumber);
				String holderName = request.getParameter("account_hn");
				boolean salaryUpdate = request.getParameter("y").equalsIgnoreCase("y")?true:false;
				savingsAccountService.updateAccount(savingsAccount);
				savingsAccount.getBankAccount().setAccountHolderName(holderName);
				savingsAccount.setSalary(salaryUpdate);
				savingsAccountService.updateAccount(savingsAccount);
				response.sendRedirect("getAll.mm");
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
				break;
		default:
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

}
