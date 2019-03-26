<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	        </div>
	    </header>
	    <section id="main">
	        <div class="container">
	            <div class="row">
	                <div class="col-xs-8 col-xs-offset-2 box">
	                    <div class="label label-default pull-right">
	                        id: ${computer.id}
	                    </div>
	                    <h1>Edit Computer</h1>
	
	                    <form action="editComputer?computerId=${computer.id}" method="POST">
	                        <input type="hidden" value="${computer.id}" id="id" name="id"/>
	                        <fieldset>
	                            <div class="form-group">
	                                <label for="computerName">Computer name</label>
	                                <input 
	                                	type="text" 
	                                	class="form-control" 
	                                	id="computerName" 
	                                	name="computerName" 
	                                	value="${computer.name}" 
	                                	required pattern=".*\S+.*" 
	                                	oninvalid="this.setCustomValidity('The name must contain at least one non white space character.')"
	                                	onchange="try{setCustomValidity('')}catch(e){}"
	                                	oninput="setCustomValidity(' ')">
	                            	<span id="emptyNameError" class="error">Computer name must be set.</span>
	                            </div>
	                            <div class="form-group">
	                                <label for="introduced">Introduced date</label>
	                                <input type="date" class="form-control" id="introduced" name="introduced" value="${computer.introduced}">
	                            </div>
	                            <div class="form-group">
	                                <label for="discontinued">Discontinued date</label>
	                                <input type="date" class="form-control" id="discontinued" name="discontinued" value="${computer.discontinued}" min="${computer.introduced}">
	                            </div>
	                            <div class="form-group">
	                                <label for="companyName">Company</label>
	                                <select class="form-control" id="companyName" name="companyName" >
	                                	<c:if test="${empty computer.company}">
	                                		<option selected value="">--</option>
	                                	</c:if>
	                                	<c:forEach items="${companies}" var="company">
	                                		<c:choose>
							            		<c:when test="${computer.company == company.name}">
							                		<option selected value="${company.name}">${company.name}</option>
							                	</c:when>
							                	<c:otherwise>
							                		<option value="${company.name}">${company.name}</option>
							                	</c:otherwise>
											</c:choose>
	                                	</c:forEach>
	                                </select>
	                            </div>            
	                        </fieldset>
	                        <div class="actions pull-right">
	                            <input type="submit" value="Edit" class="btn btn-primary">
	                            or
	                            <a href="dashboard" class="btn btn-default">Cancel</a>
	                        </div>
	                    </form>
	                </div>
	            </div>
	        </div>
	    </section>
	    
	    <section id="result">
	        <div class="container" style="margin-top: 15px;">
	            <div class="row">
	            	<div class="col-xs-8 col-xs-offset-2 box">
					    <c:choose>
				       		<c:when test="${addResult == 'succeed'}">
				        		<div class="alert alert-success" role="alert">${resultMessage}</div>
				         	</c:when>
				       	    <c:when test="${addResult == 'failed'}">
				         	    <div class="alert alert-danger" role="alert">${resultMessage}</div>
				         	</c:when>
				       	</c:choose>
			       	</div>
       			</div>
       		</div>
	    </section>
	    
	    <script src="js/jquery.min.js"></script>
	    <script src="js/editComputer.js"></script>
	</body>
</html>