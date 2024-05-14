<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
  <%@include file="common/head.jsp" %>
  <link rel="stylesheet" href="https://cdn.datatables.net/2.0.3/css/dataTables.bootstrap5.css">

</head>
<body class="hold-transition sidebar-collapse layout-top-nav">
<div class="wrapper">
  <%@include file="common/topnav.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <%@include file="common/header.jsp" %>
    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        <div class="card">
          <div class="card-header">
            <div class="row">
              <div class="col-md-6">
                <h4 id="cardTitle" class="card-title"></h4>
              </div>
              <div class="col-md-6">
                <div class="btn-group" style="float: right;">
                  <button id="addButton" onclick="javascript:Page.addOrEditRec('-1',false, false);" type="button" class="btn btn-outline btn-sm"><i class="fas fa-plus"></i> Add </button>
                  <button id="importButton" onclick="javascript:Import.upload();" type="button" class="btn btn-outline btn-sm"><i class="fas fa-file-import"></i> Import </button>
                  <!--
                  <button id="exportButton" onclick="javascript:Page.exportRecords();" type="button" class="btn btn-outline btn-sm"><i class="fas fa-download"></i> Export </button>
                  -->
                </div>
              </div>
            </div>
          </div>
          <div class="card-body">

            <div class="input-group mb-3">

              <div class="input-group-prepend">
                <span class="input-group-text">Filters </span>
              </div>

              <select id="filter-field" class="form-control">
                <option></option>
              </select>

              <select id="filter-type" class="form-control" style="max-width: 100px;">
                <option value="=">=</option>
                <option value="<"><</option>
                <option value="<="><=</option>
                <option value=">">></option>
                <option value=">=">>=</option>
                <option value="!=">!=</option>
                <option value="like">like</option>
              </select>

              <input id="filter-value" type="text" placeholder="value to filter" class="form-control">
              <span class="input-group-append">
                <button id="filter-search" class="btn btn-secondary btn-sm mr-2" onclick="Page.filterRecords();">Search</button>
                <button id="filter-clear" class="btn btn-secondary btn-sm" onclick="Page.clearFilters();">Clear Filter</button>
              </span>
            </div><!--filter-->

            <div id="tabulatorList"></div>
          </div><!--card-body-->
          <div id="mainOverlay" class="overlay">
            <i class="fas fa-3x fa-sync-alt"></i>
          </div>

        </div><!--card-->
      </div><!-- /.container-fluid -->
    </section><!-- /.content -->
  </div><!-- /.content-wrapper -->

  <%@include file="common/footer.jsp" %>
</div>
<!-- ./wrapper -->
<%@include file="common/foot.jsp" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tabulator/6.2.1/js/tabulator.min.js"></script>
<!--
<script src="${staticUrl}/plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="${staticUrl}/plugins/jquery-validation/additional-methods.min.js"></script>
-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.14.3/xlsx.full.min.js"></script>
<script src="${staticMinUrl}/js/common.js?v=<%=System.currentTimeMillis()%>"></script>
<script src="${staticMinUrl}/js/list.js?v=<%=System.currentTimeMillis()%>"></script>

<!-- Add/Edit Modal -->
<div class="modal fade" id="thisModal" tabindex="-1" role="dialog" aria-labelledby="thisModal">
  <div class="modal-dialog modal-dialog-scrollable modal-fullscreen" role="document">
    <div class="modal-content">
        <div class="modal-header">
          <h4 id="thisModalTitle" class="modal-title">Default Modal</h4>
          <!-- Modal Spinner [START]-->
          <div id="modalLoader" class="text-center" style="display: none; padding-left: 10px;">
            <div class="spinner-border" role="status">
              <span class="sr-only">Loading...</span>
            </div>
          </div>
          <!-- Modal Spinner [END]-->
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div><!--modal-header-->

        <div class="modal-body">
          <div class="card card-primary card-outline card-outline-tabs">
            <div class="card-header p-0 border-bottom-0">
              <ul class="nav nav-tabs" id="custom-tabs-four-tab" role="tablist">
                <li class="nav-item">
                  <a class="nav-link active" id="modal-details-tab" data-toggle="pill" href="#modal-details" role="tab" aria-controls="modal-details" aria-selected="true">Details</a>
                </li>

              </ul>
            </div>
            <div class="card-body">
              <div class="tab-content" id="details-tab-tabContent">
                <div class="tab-pane fade show active" id="modal-details" role="tabpanel" aria-labelledby="modal-details-tab">
                  <div class="card card-info">
                    <form id="detailsModalForm" class="form-horizontal">
                      <div class="card-body" id="model-details-html">
                      </div><!--card-body-->
                      <div class="card-footer">
                        <button id="saveRecordBtn" onclick="javascript:Page.saveRecord();" type="button" class="btn btn-outline-primary float-end">Save</button>
                      </div><!--card-footer-->
                    </form>
                  </div>
                </div><!--modal-detail-tabs-->
              </div>
            </div>

          </div><!--card-->
        </div><!--modal-body-->

        <div class="modal-footer">
          <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Close</button>
        </div><!--modal-footer-->
    </div>
  </div>
</div>
<!-- Add/Edit Modal -->

<!-- Import Modal -->
<div class="modal fade" id="importModal" tabindex="-1" role="dialog" aria-labelledby="importModal">
  <div class="modal-dialog modal-dialog-scrollable modal-fullscreen" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 id="importModalTitle" class="modal-title">Default Modal</h4>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div><!--modal-header-->

      <div class="modal-body">
        <div class="card card-primary card-outline card-outline-tabs">
          <div class="card-header p-0 border-bottom-0">
          </div>
          <div class="card-body  table-responsive p-0">
            <form id="uploadForm" enctype="multipart/form-data">
              <div class="row">
                <div class="custom-file col-md-5 mb-1">
                  <input type="file" class="custom-file-input" id="importFile" name="csvFile" accept=".xls,.xlsx,.csv"/>
                  <label class="custom-file-label" for="importFile" >Select an Excel file (csv or .xls or.xlsx is the preferredformat)</label>
                </div>
                <div class="custom-file col-md-5 mb-1 float-right">
                  <button onclick="Import.startUploadFile()" type="button" class="btn btn-outline-primary"> Upload </button>
                </div>
              </div>
            </form>
            <hr/>
            <p class="text-center" style="font-size: 13px;" id="importFileProgressBarMsg"></p>
            <!-- Modal Spinner [START]-->
            <div id="modalLoader2" class="text-center" style="display: none; padding-left: 10px;">
              <div class="spinner-border" role="status">
                <span class="sr-only">Loading...</span>
              </div>
            </div>
            <!-- Modal Spinner [END]-->
            <div id="importFileProgressBar" style="display: none; margin-top:13px; margin-bottom: 10px;" class="progress progress-striped active">
              <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
              </div>
            </div>

            <div style="padding: 5px; display: none;" id="errorMsgImport" class="callout callout-danger"></div>
            <hr/>
            <table class="table table-hover" style="display: none;" id="mainImportTable">
              <thead>
              <tr style="font-size:12px;">
                <th>Row ID</th>
                <th>Code</th>
                <th>Category</th>
                <th>Message</th>
                <th width="100"><a class="btn btn-sm btn-outline-secondary" href="javascript:Common.tableToCSV('importResponsesTR');">Download</a></th>
              </tr>
              </thead>
              <tbody style="font-size:12px;" id="importResponses"> </tbody>
            </table>
          </div><!--card-body-->
        </div><!--card-->
      </div><!--modal-body-->

      <div class="modal-footer">
        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Close</button>
      </div><!--modal-footer-->
    </div>
  </div>
</div>
<!-- Import Modal -->

</body>
</html>