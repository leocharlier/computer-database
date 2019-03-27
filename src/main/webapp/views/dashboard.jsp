<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Computer Database</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta charset="utf-8">
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
	            <h1 id="homeTitle">
	                ${nbOfComputers} Computers found
	            </h1>
	            <div id="actions" class="form-horizontal">
	                <div class="pull-left">
	                    <form id="searchForm" action="#" method="GET" class="form-inline">
	                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
	                        <input type="submit" id="searchsubmit" value="Filter by name"
	                        class="btn btn-primary" />
	                    </form>
	                </div>
	                <div class="pull-right">
	                    <a class="btn btn-success" id="addComputer" href="addComputer">Add Computer</a> 
	                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
	                </div>
	            </div>
	        </div>
	
	        <form id="deleteForm" action="dashboard?page=${page}&size=${size}" method="POST">
	            <input type="hidden" name="selection" value="">
	        </form>
	
	        <div class="container" style="margin-top: 10px;">
	            <table class="table table-striped table-bordered">
	                <thead>
	                    <tr>
							<th class="editMode" style="width: 60px; height: 22px;">
	                            <input type="checkbox" id="selectall" /> 
	                            <span style="vertical-align: top;">
	                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
	                                        <i class="fa fa-trash-o fa-lg"></i>
	                                    </a>
	                            </span>
	                        </th>
	                        <th>
	                            Computer name
	                        </th>
	                        <th>
	                            Introduced date
	                        </th>
	                        <th>
	                            Discontinued date
	                        </th>
	                        <th>
	                            Company
	                        </th>
	                    </tr>
	                </thead>
	                <tbody id="results">
	                	<c:forEach items="${computers}" var="computer">	
		                    <tr>
		                        <td class="editMode">
		                            <input type="checkbox" name="cb" class="cb" value="${computer.id}">
		                        </td>
		                        <td>
		                            <a href="editComputer?computerId=${computer.id}">${computer.name}</a>
		                        </td>
		                        <td>${computer.introduced}</td>
		                        <td>${computer.discontinued}</td>
		                        <td>${computer.company}</td>
		                    </tr>
	                    </c:forEach>
	                </tbody>
	            </table>
	        </div>
	    </section>
	
	    <footer class="navbar-fixed-bottom">
	        <div class="container text-center">
	            <ul class="pagination">
	            	<c:choose>
	            		<c:when test="${page > 1}">
	                		<li class="page-item">
	                			<a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}" aria-label="Previous">
			                      <span aria-hidden="true">&laquo;</span>
			                    </a>
			                 </li>
	                	</c:when>
	                	<c:otherwise>
	                		<li class="page-item disabled">
		                		<a class="page-link" href="#" aria-label="Previous">
			                      <span aria-hidden="true">&laquo;</span>
			                    </a>
		                    </li>
	                	</c:otherwise>
					</c:choose>

	                <c:choose>
	                	<c:when test="${nbMaxPages < 5}">
							<c:forEach var = "i" begin = "1" end ="${nbMaxPages}">
								<c:choose>
									<c:when test="${i == page}">
										<li class="page-item active"><a class="page-link" href="dashboard?page=${i}&size=${size}&search=${search}">${i}</a></li>
									</c:when>
									<c:otherwise>
										<li><a class="page-link" href="dashboard?page=${i}&size=${size}&search=${search}">${i}</a></li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
					    </c:when>
	                	<c:when test="${page == 1}">
					        <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}">${page}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}">${page+1}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+2}&size=${size}&search=${search}">${page+2}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+3}&size=${size}&search=${search}">${page+3}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+4}&size=${size}&search=${search}">${page+4}</a></li>
			                <li class="page-item disabled"><a class="page-link" href="#">...</a></li>
			                <li class="page-item"><a class="page-link" href="dashboard?page=${nbMaxPages}&size=${size}&search=${search}">${nbMaxPages}</a></li>
					    </c:when>
					    <c:when test="${page == 2}">
					        <li><a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}">${page-1}</a></li>
			                <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}">${page}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}">${page+1}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+2}&size=${size}&search=${search}">${page+2}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+3}&size=${size}&search=${search}">${page+3}</a></li>
			                <li class="page-item disabled"><a class="page-link" href="#">...</a></li>
			                <li class="page-item"><a class="page-link" href="dashboard?page=${nbMaxPages}&size=${size}&search=${search}">${nbMaxPages}</a></li>
					    </c:when>
					    <c:when test="${page == nbMaxPages-1}">
					    	<li class="page-item"><a class="page-link" href="dashboard?page=1&size=${size}&search=${search}">1</a></li>
					    	<li class="page-item disabled"><a class="page-link" href="#">...</a></li>
					        <li><a class="page-link" href="dashboard?page=${page-3}&size=${size}&search=${search}">${page-3}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page-2}&size=${size}&search=${search}">${page-2}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}">${page-1}</a></li>
			                <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}">${page}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}">${page+1}</a></li>
					    </c:when>
					    <c:when test="${page == nbMaxPages}">
					    	<li class="page-item"><a class="page-link" href="dashboard?page=1&size=${size}&search=${search}">1</a></li>
					    	<li class="page-item disabled"><a class="page-link" href="#">...</a></li>
					        <li><a class="page-link" href="dashboard?page=${page-4}&size=${size}&search=${search}">${page-4}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page-3}&size=${size}&search=${search}">${page-3}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page-2}&size=${size}&search=${search}">${page-2}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}">${page-1}</a></li>
			                <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}">${page}</a></li>
					    </c:when>
					    <c:otherwise>
					    	<c:if test="${page > 3}">
					    		<li class="page-item"><a class="page-link" href="dashboard?page=1&size=${size}&search=${search}">1</a></li>
					    		<li class="page-item disabled"><a class="page-link" href="#">...</a></li>
					    	</c:if>
					        <li><a class="page-link" href="dashboard?page=${page-2}&size=${size}&search=${search}">${page-2}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}">${page-1}</a></li>
			                <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}">${page}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}">${page+1}</a></li>
			                <li><a class="page-link" href="dashboard?page=${page+2}&size=${size}&search=${search}">${page+2}</a></li>
					    	<c:if test="${page < nbMaxPages-2}">
					    		<li class="page-item disabled"><a class="page-link" href="#">...</a></li>
					    		<li class="page-item"><a class="page-link" href="dashboard?page=${nbMaxPages}&size=${size}&search=${search}">${nbMaxPages}</a></li>
					    	</c:if>
					    </c:otherwise>
	                </c:choose>

	                <c:choose>
	            		<c:when test="${page < nbMaxPages}">
	                		<li class="page-item">
	                			<a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}" aria-label="Next">
			                      <span aria-hidden="true">&raquo;</span>
			                    </a>
			                 </li>
	                	</c:when>
	                	<c:otherwise>
	                		<li class="page-item disabled">
		                		<a class="page-link" href="#" aria-label="Next">
			                      <span aria-hidden="true">&raquo;</span>
			                    </a>
		                    </li>
	                	</c:otherwise>
					</c:choose>
	            </ul>
	
		        <div class="btn-group btn-group-sm pull-right" role="group" >
		        	<c:choose>
		        		<c:when test="${size == 10}">
	                		<a href="dashboard?page=${page}&size=10&search=${search}" class="btn btn-default active">10</a>
				        	<a href="dashboard?page=${page}&size=50&search=${search}" class="btn btn-default">50</a>
				        	<a href="dashboard?page=${page}&size=100&search=${search}" class="btn btn-default">100</a>
	                	</c:when>
	                	<c:when test="${size == 50}">
	                		<a href="dashboard?page=${page}&size=10&search=${search}" class="btn btn-default">10</a>
				        	<a href="dashboard?page=${page}&size=50&search=${search}" class="btn btn-default active">50</a>
				        	<a href="dashboard?page=${page}&size=100&search=${search}" class="btn btn-default">100</a>
	                	</c:when>
	                	<c:when test="${size == 100}">
	                		<a href="dashboard?page=${page}&size=10&search=${search}" class="btn btn-default">10</a>
				        	<a href="dashboard?page=${page}&size=50&search=${search}" class="btn btn-default">50</a>
				        	<a href="dashboard?page=${page}&size=100&search=${search}" class="btn btn-default active">100</a>
	                	</c:when>
	                	<c:otherwise>
	                		<a href="dashboard?page=${page}&size=10" class="btn btn-default">10</a>
				        	<a href="dashboard?page=${page}&size=50" class="btn btn-default">50</a>
				        	<a href="dashboard?page=${page}&size=100" class="btn btn-default">100</a>
	                	</c:otherwise>
	                </c:choose>
		        	
		        </div>
	        </div>
	
	    </footer>
		    
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/dashboard.js"></script>
	</body>
</html>