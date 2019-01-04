<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="newAccount.mm">
		<p>
			Account Holder Name: <input type="text" required maxlength="25"
				name="Enter_name" required pattern="[a-zA-Z]*"
				placeholder="Enter your Name">
		</p>
		<br>
		<p>
			Initial Balance:<input type="number" name="Enter_Balance" required
				min="0" required placeholder="Enter initial balanace">
		</p>
		<br>
		<p>
			AccountType:<br> <input type="radio" name="type" value="y">
			Salarised<br> <input type="radio" name="type" value="n">
			Not Salarised<br> <input type="submit" name="submit"
				value="submit">
		</p>
	</form>
	<div>
		<jsp:include page="homeLink.html"></jsp:include>
	</div>
</body>
</html>