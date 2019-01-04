<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="checkBalance.mm">
		<p>
			Account Number:<input type="number" name="Enter_Account_No" required
				pattern="[0-9]*" required placeholder="Enter AccountNumber">
		</p>
		<input type="submit" name="submit" value="submit">
		<div>
			<jsp:include page="homeLink.html"></jsp:include>
		</div>
	</form>
</body>
</html>