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
        			<a class="navbar-brand" href="login"> Application - Computer Database </a>
	        	</div>
	        	<ul class="nav navbar-nav navbar-right">
			      	<li><a href="login?lang=en"><img src="images/english_icon.png" alt="<spring:message code="english"/>" height="30" width="30"></a></li>
			      	<li><a href="login?lang=fr"><img src="images/french_icon.png" alt="<spring:message code="french"/>" height="30" width="30"></a></li>
			    </ul>
	        </div>
	    </header>
		
	    <section id="main">
	        <div class="container">
	        	<h1 class="welcome-message"><spring:message code="welcomemessage"/> Computer Database !</h1>
	            <div class="row">
	                
	                <div class="col-sm-6"> 
	                	<h3><spring:message code="signin"/></h3>
	                    <form:form action="loginAction" method="POST" modelAttribute="user">
	                    	<fieldset>
	                    		<div class="form-group">
			                    	<form:label path="username"><spring:message code="username"/></form:label>
	                                <spring:message code="username" var="placeholder" />
	                                <form:input path="username" 
	                                			type="text" 
	                                			class="form-control" 
	                                			placeholder="${placeholder}"
	                                			required="true"
	                                			pattern=".*\S+.*" 
	                                			oninvalid="this.setCustomValidity('The username must contain at least one non white space character.')"
	                                			onchange="try{setCustomValidity('')}catch(e){}"
												oninput="setCustomValidity(' ')"/>
								</div>
								<div class="form-group">
									<form:label path="password"><spring:message code="password"/></form:label>
									<spring:message code="password" var="placeholder" />
									<form:input path="password" 
	                                			type="password" 
	                                			class="form-control" 
	                                			placeholder="${placeholder}"
	                                			required="true"
	                                			pattern=".*\S+.*" 
	                                			oninvalid="this.setCustomValidity('The password must contain at least one non white space character.')"
	                                			onchange="try{setCustomValidity('')}catch(e){}"
												oninput="setCustomValidity(' ')"/>
								</div>
	                   		</fieldset>
	                   		<c:if test="${errorLogin}">
						        <div class="alert alert-danger" role="alert">
						        	<strong><spring:message code="errorLogin"/>.</strong>
						        </div>
						    </c:if>
		                    <div class="actions pull-right">
	                            <input type="submit" value="<spring:message code="signin"/>" class="btn btn-primary">
	                         </div>
	                    </form:form>
	                </div>
	                
	                <div class="col-sm-6"> 
	                	<h3>... <spring:message code="createyouraccount"/></h3>
	                    <form:form action="registration" method="POST" modelAttribute="user">
	                    	<fieldset>
	                    		<div class="form-group">
			                    	<form:label path="username"><spring:message code="username"/></form:label>
			                    	<spring:message code="username" var="placeholder" />
	                                <form:input path="username" 
	                                			type="text" 
	                                			class="form-control" 
	                                			placeholder="${placeholder}"
	                                			required="true"
	                                			pattern=".*\S+.*" 
	                                			oninvalid="this.setCustomValidity('The username must contain at least one non white space character.')"
	                                			onchange="try{setCustomValidity('')}catch(e){}"
												oninput="setCustomValidity(' ')"/>
								</div>
								<div class="form-group">
									<form:label path="password"><spring:message code="password"/></form:label>
									<spring:message code="password" var="placeholder" />
									<form:input path="password" 
	                                			type="password" 
	                                			class="form-control" 
	                                			placeholder="${placeholder}"
	                                			required="true"
	                                			pattern=".*\S+.*" 
	                                			oninvalid="this.setCustomValidity('The password must contain at least one non white space character.')"
	                                			onchange="try{setCustomValidity('')}catch(e){}"
												oninput="setCustomValidity(' ')"/>
								</div>
	                   		</fieldset>
	                   		<c:if test="${not empty errorUsername}">
						        <div class="alert alert-danger" role="alert">
						        	<spring:message code="errorRegistration1"/><strong>${errorUsername}</strong><spring:message code="errorRegistration2"/>.
						        </div>
						    </c:if>
		                    <div class="actions pull-right">
	                            <input type="submit" value="<spring:message code="signup"/>" class="btn btn-primary">
	                         </div>
	                    </form:form>
	                </div>
	                
	            </div>
	        </div>
	    </section>
	    
	    <script src="js/jquery.min.js"></script>
	</body>
</html>