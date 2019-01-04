<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="updateAccount.mm">
		<table>
			<tr>
				<td>Account Number:</td>
				<td><input type="text" name="accountNumber"
					value="${requestScope.account.bankAccount.accountNumber}" readonly></td>
			</tr>
			<tr>
				<td>Enter Account Holder Name</td>
				<td><input type="text" name="account_hn"
					value="${requestScope.account.bankAccount.accountHolderName }"></td>
			</tr>

			<tr>
				<td>Select whether salaried or not</td>
				<td><input type="radio" name="y" value="y"
					${requestScope.account.salary==true?"checked":""}>salaried
					<input type="radio" name="y" value="n"
					${requestScope.account.salary==true?"":"checked"}>not
					salaried</td>
			</tr>
			<tr>
				<td><input type="submit" name="submit" value="submit"></td>
			</tr>
		</table>
	</form>
</body>
</html>