<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="amountwithdraw.mm">
		<p>
			Account Number:<input type="number" name="Enter_Account_No" required
				pattern="[0-9]*" required placeholder="Enter AccountNumber">
		</p>
		<p>
			Amount:<input type="number" name="Enter_Amount" required min="0"
				required placeholder="Enter amount">
		</p>
		<input type="submit" name="submit" value="submit">
		<div>
			<jsp:include page="homeLink.html"></jsp:include>
		</div>
	</form>
</body>
</html>