<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
	        	<a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
	        	<ul class="nav navbar-nav navbar-right">
			      	<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown">
	       					<span class="glyphicon glyphicon-user menu-icon" aria-hidden="true"></span><spring:message code="hi"/>, ${user} !
	     				</a>
					    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
					    	<a href="newUser">
					    		<div class="dropdown-menu-item">
							    	<span class="glyphicon glyphicon-plus menu-icon"></span>
							    	<spring:message code="newuser"/>
							    </div>
					    	</a>
					    	<a href="editComputer?computerId=${computer.id}&lang=en">
					    		<div class="dropdown-menu-item">
							    	<span class="menu-icon"><img src="images/english_icon.png" height="17" width="17"></span>
							    	<spring:message code="english"/>
							    </div>
					    	</a>
					    	<a href="editComputer?computerId=${computer.id}&lang=fr">
					    		<div class="dropdown-menu-item">
							    	<span class="menu-icon"><img src="images/french_icon.png" height="17" width="17"></span>
							    	<spring:message code="french"/>
							    </div>
					    	</a>
					    	<a href="logout">
					    		<div class="dropdown-menu-item">
							    	<span class="glyphicon glyphicon-log-out menu-icon" aria-hidden="true"></span>
							    	<spring:message code="logout"/>
							    </div>
					    	</a>
					    </div>
				   	 </li>
				</ul>
	        </div>
	    </header>
	    
   		<section id="main">
	        <div class="container">
	            <div class="row">
	                <div class="col-xs-8 col-xs-offset-2 box">
	                    <div class="label label-default pull-right">
	                        id: ${computer.id}
	                    </div>
	                    <h1><spring:message code="editcomputer"/></h1>
	
	                    <form:form action="editComputer?computerId=${computer.id}" method="POST" modelAttribute="computerDto">
	                        <form:input path="id" type="hidden" value="${computer.id}"/>
	                        <fieldset>
	                            <div class="form-group">
	                                <form:label path="name"><spring:message code="computername"/></form:label>
	                                <form:input
	                                	path="name"
	                                	type="text" 
	                                	class="form-control"
	                                	value="${computer.name}"/>
	                            	<span id="emptyNameError" class="error"><spring:message code="computernameerror"/>.</span>
	                            </div>
	                            <div class="form-group">
	                                <form:label path="introduced"><spring:message code="introduceddate"/></form:label>
	                                <form:input path="introduced" type="date" class="form-control" value="${computer.introduced}"/>
	                            </div>
	                            <div class="form-group">
	                                <form:label path="discontinued"><spring:message code="discontinueddate"/></form:label>
	                                <form:input path="discontinued" type="date" class="form-control" value="${computer.discontinued}" min="${computer.introduced}"/>
	                            </div>
	                            <div class="form-group">
	                                <form:label path="company"><spring:message code="company"/></form:label>
	                                <form:select path="company" class="form-control">
	                                	<c:if test="${empty computer.company}">
	                                		<form:option selected="true" value="">--</form:option>
	                                	</c:if>
	                                	<c:forEach items="${companies}" var="company">
	                                		<c:choose>
							            		<c:when test="${computer.company == company.name}">
							                		<form:option selected="true" value="${company.name}">${company.name}</form:option>
							                	</c:when>
							                	<c:otherwise>
							                		<form:option value="${company.name}">${company.name}</form:option>
							                	</c:otherwise>
											</c:choose>
	                                	</c:forEach>
	                                </form:select>
	                            </div>            
	                        </fieldset>
	                        <div class="actions pull-right">
	                            <input type="submit" value="<spring:message code="editbutton"/>" class="btn btn-primary">
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
	    
	    <c:if test="${errorName}">
	    	<section id="result">
		        <div class="container" style="margin-top: 15px;">
		            <div class="row">
		            	<div class="col-xs-8 col-xs-offset-2 box">
					        <div class="alert alert-danger" role="alert">
					        	<strong><spring:message code="computernameerror"/>.</strong>
					        </div>
				       	</div>
	       			</div>
	       		</div>
		    </section>
	    </c:if>
	    
	    <script src="js/jquery.min.js"></script>
	    <script src="js/bootstrap.min.js"></script>
	    <script src="js/validator.js"></script>
	</body>
</html>