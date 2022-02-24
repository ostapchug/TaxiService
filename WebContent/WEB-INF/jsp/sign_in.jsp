<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<!DOCTYPE html>
<html>
<c:set var="title" value="Taxi Service | Sign In" />
<c:set var="localePath" value="?command=sign_in_page&locale="/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/jspf/navbar.jspf"%>
	<div class="container">
		<div class="page-header">
			<h2>
				<fmt:message key="general.anchor.sign_in" />
			</h2>
		</div>
		<div class="row">
			<div class="col-sm-offset-3 col-md-6 inf-content">
				<h3>
					<fmt:message key="sign_in_jsp.anchor.info" />
				</h3>
				<hr>
				<form class="form-horizontal" action="?command=sign_in"
					method="post">
					<!-- Phone input-->
					<div class="form-group">
						<label class="control-label col-sm-3" for="phone">
						<fmt:message key="general.label.anchor.phone" />
						</label>
						<div class="col-sm-8">
							<input type="text" name="phone"	placeholder="<fmt:message key="general.label.anchor.phone_placeholder"/>" value="<c:out value="${phone}"/>" class="form-control" required>
							<span class="help-block"> 
								<c:if test="${not empty errorPhone}">
									<fmt:message key="${errorPhone}"/>
								</c:if>
							</span>
						</div>
					</div>

					<!-- Password input-->
					<div class="form-group">
						<label class="control-label col-sm-3" for="password">
						<fmt:message key="general.label.anchor.password" />
						</label>
						<div class="col-sm-8">
							<input type="password" name="password" placeholder="<fmt:message key="general.label.anchor.password_placeholder"/>" class="form-control" required> 
							<span class="help-block">
								<c:if test="${not empty errorPassword}">
									<fmt:message key="${errorPassword}"/>
								</c:if>
							</span>
						</div>
					</div>

					<!-- Submit Button -->
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-8">
							<button type="submit" class="btn btn-primary">
								<fmt:message key="general.button.anchor.submit"/>
							</button>
							&nbsp;
							<fmt:message key="sign_in_jsp.anchor.hint"/>
							<a href="app?command=sign_up_page"><fmt:message	key="general.anchor.sign_up"/></a>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>