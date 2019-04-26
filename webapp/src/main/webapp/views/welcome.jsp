<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Computer Database</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
		<link href="css/font-awesome.css" rel="stylesheet" media="screen">
		<link href="css/main.css" rel="stylesheet" media="screen">
	</head>
	<body>
	    <header class="navbar navbar-inverse navbar-fixed-top">
	        <div class="container">
	            <div class="pull-left">
        			<a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
	        	</div>
	        	<ul class="nav navbar-nav navbar-right">
			      	<li><a href="addComputer?lang=en"><img src="images/english_icon.png" alt="Anglais" height="30" width="30"></a></li>
			      	<li><a href="addComputer?lang=fr"><img src="images/french_icon.png" alt="Français" height="30" width="30"></a></li>
			    </ul>
	        </div>
	    </header>
	
	    <section id="main">
	        <div class="container">
	            <div class="row">
	                <div class="col-xs-8 col-xs-offset-2 box">
	                    <h1>Welcome !</h1>
                            <div class="form-group">
			                    <form:form action="userRegistration" method="POST" modelAttribute="user">
			                    	<fieldset>
				                    	<form:label path="username">Username</form:label>
		                                <form:input path="username" 
		                                			type="text" 
		                                			class="form-control" 
		                                			placeholder="Username"
		                                			required="true"
		                                			pattern=".*\S+.*" 
		                                			oninvalid="this.setCustomValidity('The id must contain at least one non white space character.')"
		                                			onchange="try{setCustomValidity('')}catch(e){}"
													oninput="setCustomValidity(' ')"/>
										<form:label path="password">Password</form:label>
										<form:input path="password" 
		                                			type="text" 
		                                			class="form-control" 
		                                			placeholder="Password"
		                                			required="true"
		                                			pattern=".*\S+.*" 
		                                			oninvalid="this.setCustomValidity('The password must contain at least one non white space character.')"
		                                			onchange="try{setCustomValidity('')}catch(e){}"
													oninput="setCustomValidity(' ')"/>
			                   		</fieldset>
				                    <div class="actions pull-right">
			                            <input type="submit" value="Register" class="btn btn-primary">
			                         </div>
			                    </form:form>
		                    </div>
	                </div>
	            </div>
	        </div>
	    </section>
	    
	    <script src="js/jquery.min.js"></script>
	</body>
</html>