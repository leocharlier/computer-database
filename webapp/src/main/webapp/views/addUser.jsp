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
					    	<a href="newUser?lang=en">
					    		<div class="dropdown-menu-item">
							    	<span class="menu-icon"><img src="images/english_icon.png" height="17" width="17"></span>
							    	<spring:message code="english"/>
							    </div>
					    	</a>
					    	<a href="newUser?lang=fr">
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
	                    <h1><spring:message code="addnewuser"/></h1>
	                    <form action="addUser" method="POST">
	                        <fieldset>
	                            <div class="form-group">
	                                <label for="username"><spring:message code="username"/></label>
	                                <input type="text" 
	                                	class="form-control" 
	                                	id="username" 
	                                	name="username" 
	                                	placeholder="<spring:message code="username"/>" 
	                                	/>
	                            </div>
	                            <div class="form-group">
	                                <label for="username"><spring:message code="password"/></label>
	                                <input type="text" 
	                                	class="form-control" 
	                                	id="password" 
	                                	name="password" 
	                                	placeholder="<spring:message code="password"/>" 
	                                	/>
	                            </div>
	                            <div class="form-group">
	                                <label for="authority"><spring:message code="authority"/></label>
	                                <select class="form-control" id="authority" name="authority">
	                                    <option value="ROLE_USER"><spring:message code="user"/></option>
	                                    <option value="ROLE_ADMIN"><spring:message code="admin"/></option>
	                                </select>
	                            </div>                  
	                        </fieldset>
	                        <div class="actions pull-right">
	                            <input type="submit" value="<spring:message code="addnewuser"/>" class="btn btn-primary">
	                            <spring:message code="or"/>
	                            <a href="dashboard" class="btn btn-default"><spring:message code="cancel"/></a>
	                        </div>
	                    </form>
	                </div>
	            </div>
	        </div>
	    </section>
	    
	    <c:if test="${not empty successUsername}">
	    	<section id="result">
		        <div class="container" style="margin-top: 15px;">
		            <div class="row">
		            	<div class="col-xs-8 col-xs-offset-2 box">
					        <div class="alert alert-success" role="alert">
					        	<spring:message code="successadduser1"/><strong>${successUsername}</strong><spring:message code="successadduser2"/>. <a href="dashboard"><spring:message code="backtodashboard"/></a>
					        </div>
				       	</div>
	       			</div>
	       		</div>
		    </section>
	    </c:if>
	    
	    <c:if test="${not empty errorUsername}">
	    	<section id="result">
		        <div class="container" style="margin-top: 15px;">
		            <div class="row">
		            	<div class="col-xs-8 col-xs-offset-2 box">
					        <div class="alert alert-danger" role="alert">
					        	<spring:message code="errorRegistration1"/><strong>${errorUsername}</strong><spring:message code="errorRegistration2"/>. <a href="dashboard"><spring:message code="backtodashboard"/></a>
					        </div>
				       	</div>
	       			</div>
	       		</div>
		    </section>
	    </c:if>
	    
	    <c:if test="${not empty incorrectField}">
	    	<section id="result">
		        <div class="container" style="margin-top: 15px;">
		            <div class="row">
		            	<div class="col-xs-8 col-xs-offset-2 box">
					        <div class="alert alert-danger" role="alert">
					        	<spring:message code="errorAddUser1"/><strong><spring:message code="${incorrectField}"/></strong><spring:message code="errorAddUser2"/>.
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