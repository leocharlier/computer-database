<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
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
        	<div class="pull-left">
        		<a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
        		<p class="user-label">User : ${user}</p>
        	</div>
        	<ul class="nav navbar-nav navbar-right">
		      	<li><a href="dashboard?page=${page}&size=${size}&search=${search}&sort=${sort}&lang=en"><img src="images/english_icon.png" alt="Anglais" height="30" width="30"></a></li>
		      	<li><a href="dashboard?page=${page}&size=${size}&search=${search}&sort=${sort}&lang=fr"><img src="images/french_icon.png" alt="Français" height="30" width="30"></a></li>
		    	<li><a href="logout" class="btn btn-danger logout-button">Logout</a></li>
		    </ul>
        </div>
	    </header>
	
	    <section id="main">
	        <div class="container">
	            <h1 id="homeTitle">
	                ${nbOfComputers} <spring:message code="computerfound"/>
	            </h1>
	            <div id="actions" class="form-horizontal">
	                <div class="pull-left">
	                    <form id="searchForm" action="#" method="GET" class="form-inline">
	                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="<spring:message code="search"/>" />
	                        <input type="submit" id="searchsubmit" value="<spring:message code="filter"/>"
	                        class="btn btn-primary" />
	                        <c:if test="${not empty search}">
		                    	<a class="btn btn-default" id="backToList" href="dashboard"><spring:message code="allcomputers"/></a>
		                    </c:if>
	                    </form>
	                </div>
	                
	                <security:authorize access="hasRole('ROLE_ADMIN')">
		                <div class="pull-right">
		                    <a class="btn btn-success" id="addComputer" href="addComputer"><spring:message code="add"/></a>
		                    <span id="editText" style="display:none"><spring:message code="edit"/></span>
		                    <span id="viewText" style="display:none"><spring:message code="view"/></span>
		                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><spring:message code="edit"/></a>
		                </div>
		            </security:authorize>
	            </div>
	        </div>
	
	        <form id="deleteForm" action="deleteComputers" method="POST">
	            <input type="hidden" name="selection" value="">
	        </form>
	
	        <div class="container" style="margin-top: 10px;">
	        	<c:choose>
	        		<c:when test="${noComputersFound}">
				        <div class="alert alert-warning" role="alert">
				        	<c:choose>
				        		<c:when test="${not empty search}">
				        			<spring:message code="nosearchresult"/> "<strong>${search}</strong>"... <a href="dashboard"><spring:message code="backtodashboard"/>.</a> 
				        		</c:when>
				        		<c:otherwise>
				        			<spring:message code="nocomputerindb"/>... <a href="addComputer"><spring:message code="addfirstcomputer"/> !</a> 
				        		</c:otherwise>
				        	</c:choose>
				        </div>
	        		</c:when>
	        		<c:otherwise>
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
			                        	<c:choose>
			                        		<c:when test="${empty sort || sort == 'nameAsc'}">
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=nameDesc">
			                        				<spring:message code="computername"/>
			                        				<span class="glyphicon glyphicon-sort-by-alphabet sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:when>
			                        		<c:when test="${sort == 'nameDesc'}">
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=nameAsc">
			                        				<spring:message code="computername"/>
			                        				<span class="glyphicon glyphicon-sort-by-alphabet-alt sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:when>
			                        		<c:otherwise>
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=nameAsc">
			                        				<spring:message code="computername"/>
			                        				<span class="glyphicon glyphicon-sort sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:otherwise>
			                        	</c:choose>
			                        </th>
			                        <th>
			                        	<c:choose>
			                        		<c:when test="${sort == 'introducedAsc'}">
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=introducedDesc">
			                        				<spring:message code="introduceddate"/>
			                        				<span class="glyphicon glyphicon-sort-by-order sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:when>
			                        		<c:when test="${sort == 'introducedDesc'}">
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=introducedAsc">
			                        				<spring:message code="introduceddate"/>
			                        				<span class="glyphicon glyphicon-sort-by-order-alt sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:when>
			                        		<c:otherwise>
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=introducedAsc">
			                        				<spring:message code="introduceddate"/>
			                        				<span class="glyphicon glyphicon-sort sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:otherwise>
			                        	</c:choose>
			                        </th>
			                        <th>
			                        	<c:choose>
			                        		<c:when test="${sort == 'discontinuedAsc'}">
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=discontinuedDesc">
			                        				<spring:message code="discontinueddate"/>
			                        				<span class="glyphicon glyphicon-sort-by-order sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:when>
			                        		<c:when test="${sort == 'discontinuedDesc'}">
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=discontinuedAsc">
			                        				<spring:message code="discontinueddate"/>
			                        				<span class="glyphicon glyphicon-sort-by-order-alt sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:when>
			                        		<c:otherwise>
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=discontinuedAsc">
			                        				<spring:message code="discontinueddate"/>
			                        				<span class="glyphicon glyphicon-sort sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:otherwise>
			                        	</c:choose>
			                        </th>
			                        <th>
			                            <c:choose>
			                        		<c:when test="${sort == 'companyAsc'}">
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=companyDesc">
			                        				<spring:message code="company"/>
			                        				<span class="glyphicon glyphicon-sort-by-alphabet sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:when>
			                        		<c:when test="${sort == 'companyDesc'}">
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=companyAsc">
			                        				<spring:message code="company"/>
			                        				<span class="glyphicon glyphicon-sort-by-alphabet-alt sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:when>
			                        		<c:otherwise>
			                        			<a href="dashboard?page=${page}&size=${size}&search=${search}&sort=companyAsc">
			                        				<spring:message code="company"/>
			                        				<span class="glyphicon glyphicon-sort sort-icon" aria-hidden="true"></span>
			                        			</a>
			                        		</c:otherwise>
			                        	</c:choose>
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
				                        	<security:authorize access="hasRole('ROLE_ADMIN')">
				                            	<a href="editComputer?computerId=${computer.id}">${computer.name}</a>
				                        	</security:authorize>
				                        	<security:authorize access="hasRole('ROLE_USER')">
				                            	${computer.name}
				                        	</security:authorize>
				                        </td>
				                        <td>${computer.introduced}</td>
				                        <td>${computer.discontinued}</td>
				                        <td>${computer.company}</td>
				                    </tr>
			                    </c:forEach>
			                </tbody>
			            </table>
	        		</c:otherwise>
	        	</c:choose>
	        </div>
	    </section>
	
		<c:if test="${not noComputersFound}">
			<footer class="navbar-fixed-bottom">
		        <div class="container text-center">
		            <ul class="pagination">
		            	<c:choose>
		            		<c:when test="${page > 1}">
		                		<li class="page-item">
		                			<a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}&sort=${sort}" aria-label="Previous">
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
											<li class="page-item active"><a class="page-link" href="dashboard?page=${i}&size=${size}&search=${search}&sort=${sort}">${i}</a></li>
										</c:when>
										<c:otherwise>
											<li><a class="page-link" href="dashboard?page=${i}&size=${size}&search=${search}&sort=${sort}">${i}</a></li>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						    </c:when>
		                	<c:when test="${page == 1}">
						        <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}&sort=${sort}">${page}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}&sort=${sort}">${page+1}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+2}&size=${size}&search=${search}&sort=${sort}">${page+2}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+3}&size=${size}&search=${search}&sort=${sort}">${page+3}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+4}&size=${size}&search=${search}&sort=${sort}">${page+4}</a></li>
				                <li class="page-item disabled"><a class="page-link" href="#">...</a></li>
				                <li class="page-item"><a class="page-link" href="dashboard?page=${nbMaxPages}&size=${size}&search=${search}&sort=${sort}">${nbMaxPages}</a></li>
						    </c:when>
						    <c:when test="${page == 2}">
						        <li><a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}&sort=${sort}">${page-1}</a></li>
				                <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}&sort=${sort}">${page}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}&sort=${sort}">${page+1}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+2}&size=${size}&search=${search}&sort=${sort}">${page+2}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+3}&size=${size}&search=${search}&sort=${sort}">${page+3}</a></li>
				                <li class="page-item disabled"><a class="page-link" href="#">...</a></li>
				                <li class="page-item"><a class="page-link" href="dashboard?page=${nbMaxPages}&size=${size}&search=${search}&sort=${sort}">${nbMaxPages}</a></li>
						    </c:when>
						    <c:when test="${page == nbMaxPages-1}">
						    	<li class="page-item"><a class="page-link" href="dashboard?page=1&size=${size}&search=${search}&sort=${sort}">1</a></li>
						    	<li class="page-item disabled"><a class="page-link" href="#">...</a></li>
						        <li><a class="page-link" href="dashboard?page=${page-3}&size=${size}&search=${search}&sort=${sort}">${page-3}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page-2}&size=${size}&search=${search}&sort=${sort}">${page-2}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}&sort=${sort}">${page-1}</a></li>
				                <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}&sort=${sort}">${page}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}&sort=${sort}">${page+1}</a></li>
						    </c:when>
						    <c:when test="${page == nbMaxPages}">
						    	<li class="page-item"><a class="page-link" href="dashboard?page=1&size=${size}&search=${search}&sort=${sort}">1</a></li>
						    	<li class="page-item disabled"><a class="page-link" href="#">...</a></li>
						        <li><a class="page-link" href="dashboard?page=${page-4}&size=${size}&search=${search}&sort=${sort}">${page-4}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page-3}&size=${size}&search=${search}&sort=${sort}">${page-3}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page-2}&size=${size}&search=${search}&sort=${sort}">${page-2}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}&sort=${sort}">${page-1}</a></li>
				                <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}&sort=${sort}">${page}</a></li>
						    </c:when>
						    <c:otherwise>
						    	<c:if test="${page > 3}">
						    		<li class="page-item"><a class="page-link" href="dashboard?page=1&size=${size}&search=${search}&sort=${sort}">1</a></li>
						    		<li class="page-item disabled"><a class="page-link" href="#">...</a></li>
						    	</c:if>
						        <li><a class="page-link" href="dashboard?page=${page-2}&size=${size}&search=${search}&sort=${sort}">${page-2}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page-1}&size=${size}&search=${search}&sort=${sort}">${page-1}</a></li>
				                <li class="page-item active"><a class="page-link" href="dashboard?page=${page}&size=${size}&search=${search}&sort=${sort}">${page}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}&sort=${sort}">${page+1}</a></li>
				                <li><a class="page-link" href="dashboard?page=${page+2}&size=${size}&search=${search}&sort=${sort}">${page+2}</a></li>
						    	<c:if test="${page < nbMaxPages-2}">
						    		<li class="page-item disabled"><a class="page-link" href="#">...</a></li>
						    		<li class="page-item"><a class="page-link" href="dashboard?page=${nbMaxPages}&size=${size}&search=${search}&sort=${sort}">${nbMaxPages}</a></li>
						    	</c:if>
						    </c:otherwise>
		                </c:choose>
	
		                <c:choose>
		            		<c:when test="${page < nbMaxPages}">
		                		<li class="page-item">
		                			<a class="page-link" href="dashboard?page=${page+1}&size=${size}&search=${search}&sort=${sort}" aria-label="Next">
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
		                		<a href="dashboard?page=${page}&size=10&search=${search}&sort=${sort}" class="btn btn-default active">10</a>
					        	<a href="dashboard?page=${page}&size=50&search=${search}&sort=${sort}" class="btn btn-default">50</a>
					        	<a href="dashboard?page=${page}&size=100&search=${search}&sort=${sort}" class="btn btn-default">100</a>
		                	</c:when>
		                	<c:when test="${size == 50}">
		                		<a href="dashboard?page=${page}&size=10&search=${search}&sort=${sort}" class="btn btn-default">10</a>
					        	<a href="dashboard?page=${page}&size=50&search=${search}&sort=${sort}" class="btn btn-default active">50</a>
					        	<a href="dashboard?page=${page}&size=100&search=${search}&sort=${sort}" class="btn btn-default">100</a>
		                	</c:when>
		                	<c:when test="${size == 100}">
		                		<a href="dashboard?page=${page}&size=10&search=${search}&sort=${sort}" class="btn btn-default">10</a>
					        	<a href="dashboard?page=${page}&size=50&search=${search}&sort=${sort}" class="btn btn-default">50</a>
					        	<a href="dashboard?page=${page}&size=100&search=${search}&sort=${sort}" class="btn btn-default active">100</a>
		                	</c:when>
		                	<c:otherwise>
		                		<a href="dashboard?page=${page}&size=10&sort=${sort}" class="btn btn-default">10</a>
					        	<a href="dashboard?page=${page}&size=50&sort=${sort}" class="btn btn-default">50</a>
					        	<a href="dashboard?page=${page}&size=100&sort=${sort}" class="btn btn-default">100</a>
		                	</c:otherwise>
		                </c:choose>
			        </div>
		        </div>
		    </footer>
		</c:if>
	    
		    
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/dashboard.js"></script>
	</body>
</html>