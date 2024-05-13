  <%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
  <%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
  <spring:eval expression="@environment.getProperty('app.static-min-url')" var="staticMinUrl" />
  <spring:eval expression="@environment.getProperty('server.servlet.context-path')" var="appPath" />
  <spring:eval expression="@environment.getProperty('google.gg-captcha-site-key')" var="reCapSiteKey" />
  <spring:eval expression="@environment.getProperty('app.security-service')" var="securityUrl" />
  <c:set var = "requestPath" scope = "request" value = "${requestScope['javax.servlet.forward.request_uri']}"/>


  <script>
    var appPath         = '<spring:eval expression="@environment.getProperty('server.servlet.context-path')" />';
    var reqPath         = '${requestScope['javax.servlet.forward.request_uri']}';
    var maxFileSize     = <spring:eval expression="@environment.getProperty('spring.servlet.multipart.max-file-size')" />;
    var staticMinUrl    = '${staticMinUrl}';
    var pageCollection  = '${requestScope['collection']}';
    var pageTitle       = '${requestScope['collectionName']}';
    var pageMetaData    =  JSON.parse('${requestScope['metaData']}');
    var serverError     =  '${requestScope['serverError']}';
    var pageParentID    = '${param.pid}';
  </script>

   <!-- ### AdminLTE [START] -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ZCRUD</title>

  <!-- Google Font: Montserrat -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">

  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
  <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
  <!--
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/icheck-bootstrap/3.0.1/icheck-bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/css/select2.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-css/1.4.6/select2-bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/4.6.1/css/bootstrap.min.css"/>
  -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/3.2.0/css/adminlte.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.datatables.net/2.0.3/css/dataTables.bootstrap5.css">

  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tabulator/6.2.1/css/tabulator.min.css" integrity="sha512-RYFH4FFdhD/FdA+OVEbFVqd5ifR+Dnx2M7eWcmkcMexlIoxNgm89ieeVyHYb8xChuYBtbrasMTlo02cLnidjtQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tabulator/6.2.1/css/tabulator_bootstrap4.min.css" integrity="sha512-qDEgvDbdp7tq+ytU/OgCzWfvbfdEe3pv0yEOMz/gurMcR0BWNgIF6I4VKeoACEj5E5PFf1uo3Vzuwk/ga9zeUg==" crossorigin="anonymous" referrerpolicy="no-referrer" />


  <!-- ### AdminLTE [END] -->

  <link rel="stylesheet" href="${staticMinUrl}/css/app.css">
  <link rel="icon" type="image/x-icon" href="${staticMinUrl}/images/jm.svg">

