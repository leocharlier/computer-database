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
	                    <h1><spring:message code="add"/></h1>
	                    <form:form action="dashboard" method="POST" modelAttribute="computerDto">
	                        <fieldset>
	                            <div class="form-group">
	                                <form:label path="name"><spring:message code="computername"/></form:label>
	                                <spring:message code="computername" var="placeholder" />
	                                <form:input path="name" 
	                                			type="text" 
	                                			class="form-control" 
	                                			placeholder="${placeholder}"
	                                			required="true"
	                                			pattern=".*\S+.*" 
	                                			oninvalid="this.setCustomValidity('The name must contain at least one non white space character.')"
	                                			onchange="try{setCustomValidity('')}catch(e){}"
												oninput="setCustomValidity(' ')"/>
	                            	<span id="emptyNameError" class="error"><spring:message code="computernameerror"/>.</span>
	                            </div>
	                            <div class="form-group">
	                                <form:label path="introduced"><spring:message code="introduceddate"/></form:label>
	                                <form:input path="introduced" type="date" class="form-control" min="1950-01-01"/>
	                            </div>
	                            <div class="form-group">
	                                <form:label path="discontinued"><spring:message code="discontinueddate"/></form:label>
	                                <form:input path="discontinued" type="date" class="form-control" disabled="true"/>
	                            </div>
	                            <div class="form-group">
	                                <form:label path="company"><spring:message code="company"/></form:label>
	                                <form:select class="form-control" path="company">
	                                	<form:option selected="true" value="" label="--"/>
	                                	<c:forEach items="${companies}" var="company">
							                <form:option value="${company.name}" label="${company.name}"/>
										</c:forEach>
	                                </form:select>
	                            </div>                  
	                        </fieldset>
	                        <div class="actions pull-right">
	                            <input type="submit" value="<spring:message code="addbutton"/>" class="btn btn-primary">
	                            <spring:message code="or"/>
	                            <a href="dashboard" class="btn btn-default"><spring:message code="cancel"/></a>
	                        </div>
	                    </form:form>
	                </div>
	            </div>
	        </div>
	    </section>
	    
	    <c:if test="${not empty resultMessage}">
	    	<section id="result">
		        <div class="container" style="margin-top: 15px;">
		            <div class="row">
		            	<div class="col-xs-8 col-xs-offset-2 box">
					        <div class="alert alert-success" role="alert">
					        	${resultMessage} <a href="dashboard"><spring:message code="backtodashboard"/>.</a>
					        </div>
				       	</div>
	       			</div>
	       		</div>
		    </section>
	    </c:if>
	    
	    <script src="js/jquery.min.js"></script>
	    <script src="js/validator.js"></script>
	</body>
</html>