<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="common/head.jsp" %>
</head>
<body class="hold-transition sidebar-collapse layout-top-nav">
<div class="wrapper">
    <%@include file="common/topnav.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">

        <!-- Content Header (Page header) -->
        <div class="content-header">
            <div class="container-fluid">
                <div class="row mb-2">
                    <div class="col-sm-6">
                        <h1 id="titleText" class="m-0">401/403 Unauthorized & Forbidden Error</h1>
                    </div><!-- /.col -->
                    <div class="col-sm-6">
                        <ol class="breadcrumb float-sm-right">
                            <li class="breadcrumb-item"><a href="${appPath}">Home</a></li>
                            <li id="bcText" class="breadcrumb-item active">401/403 Unauthorized & Forbidden </li>
                        </ol>
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.container-fluid -->
        </div>
        <!-- /.content-header -->

        <section class="content">
            <div class="error-page">
                <h2 class="headline text-warning"> 401</h2>
                <div class="error-content">
                    <h3><i class="fas fa-exclamation-triangle text-warning"></i> You are not authorized to access this resource!</h3>
                    <p>
                        Please contact your IT administrator! If you need access!
                        Meanwhile, you may <a href="./">return to dashboard</a>
                    </p>
                </div>
            </div>
        </section>

    </div><!-- /.content-wrapper -->

    <%@include file="common/footer.jsp" %>
</div>
<!-- ./wrapper -->
<%@include file="common/foot.jsp" %>
</body>
</html>