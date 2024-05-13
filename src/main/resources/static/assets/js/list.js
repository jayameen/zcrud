let Html = {
    labelHtml : function(attrDisplayName, attrIDName, attrVal, required){
        return `
        <div class="form-group row">
          <label style="background: #f1f1f1;" for="mod_`+attrID+`" class="col-sm-2 col-form-label">`+attrDisplayName+`</label>
          <div class="col-sm-10">
            <input type="text" readonly="readonly" class="form-control" id="mod_`+attrIDName+`" placeholder="" value="`+attrVal+`"/>
          </div>
        </div>
        `;
    },
    textHtml : function(attrDisplayName, attrIDName, attrVal, required){
        return `
        <div class="form-group row">
          <label style="background: #f1f1f1;" for="mod_`+attrIDName+`" class="col-sm-2 col-form-label `+required+` ">`+attrDisplayName+`</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="mod_`+attrIDName+`" placeholder="" value="`+attrVal+`"/>
          </div>
        </div>
        `;
    },
    textAreaHtml : function(attrDisplayName, attrIDName, attrVal, required){
        return `
        <div class="form-group row">
          <label style="background: #f1f1f1;"  for="mod_`+attrIDName+`" class="col-sm-2 col-form-label `+required+` ">`+attrDisplayName+`</label>
          <div class="col-sm-10">
            <textarea rows="3" class="form-control" id="mod_`+attrIDName+`" placeholder="" value="`+attrVal+`"></textarea>
          </div>
        </div>
        `;
    },
    checkHtml : function(attrDisplayName, attrIDName, attrVal, required){
        return `
            <div class="form-group`+required+` row">
                <label style="background: #f1f1f1;"  for="mod_`+attrIDName+`" class="col-sm-2 col-form-label `+required+` ">`+attrDisplayName+`</label>
                <div class="col-sm-10">
                <div class="form-check">
                    <input style="margin-top:10px;" type="checkbox" class="form-check-input" id="mod_`+attrIDName+`">
                </div>
                </div>
            </div>
        `;
    },
    select_staticHtnl : function(attrDisplayName, attrIDName, attrVal, required, obj){
        let selectHtml = '';
        selectHtml += '<div class="form-group row">';
            selectHtml += '<label style="background: #f1f1f1;"  for="mod_'+attrIDName+'" class="col-sm-2 col-form-label '+required+' ">'+attrDisplayName+'</label>';
                selectHtml += '<div class="col-sm-10">';
                    selectHtml += '<select id="mod_'+attrIDName+'" class="form-control">';
                    $(obj["select_static_data"]).each(function (i,rec){
                        selectHtml += '<option value="'+rec["value"]+'">'+rec["htmlData"]+'</option>';
                    });
                    selectHtml += '</select>';
            selectHtml += '</div>';
        selectHtml += '</div>';
       return selectHtml;
    },
    radioHtml : function(attrDisplayName, attrIDName, attrVal, required, obj){
        let radioHtml = '';
        radioHtml += '<div class="form-group row">';
            radioHtml += '<label style="background: #f1f1f1;"  for="mod_'+attrIDName+'" class="col-sm-2 col-form-label '+required+' ">'+attrDisplayName+'</label>';
            radioHtml += '<div class="col-sm-10">';
                $(obj["radio_data"]).each(function (i,rec){
                    radioHtml += '<div class="form-check">';
                    radioHtml += '<input class="form-check-input" type="radio" id="'+rec["value"]+'" name="mod_'+attrIDName+'" value="'+rec["value"]+'">';
                    radioHtml += '<label for="'+rec["value"]+'" class="form-check-label">'+rec["value"]+'</label>';
                    radioHtml += '</div>';
                });
            radioHtml += '</div>';
        radioHtml += '</div>';
        return radioHtml;
    },
    fileHtml : function(attrDisplayName, attrIDName, attrVal, required){
        return `
            <div class="form-group`+required+` row">
                <label style="background: #f1f1f1;"  for="mod_`+attrIDName+`" class="col-sm-2 col-form-label `+required+` ">`+attrDisplayName+`</label>
                <div class="col-sm-10">
                    <form id="fileUploadForm" enctype="multipart/form-data">
                        <div class="custom-file">
                          <input type="file" class="custom-file-input" id="fileUpload" name="fileUpload" accept=".pdf,.png,.jpg,.jpeg,.xls,.xlsx,.csv"/>
                          <label class="custom-file-label" for="importFile" >Select file (pdf or png or jpg/jpeg or csv or .xls or.xlsx is the preferredformat)</label>
                        </div>
                    </form>
                </div>
            </div>
        `;
    },
    getHtml:function(obj){
        let required = '';
        if(obj["mandatory"] && obj["mandatory"] == true) { required = " required"; };
        if(Page.edit && obj["displayType"] == 'label'){
            return Html.labelHtml(obj["displayName"], obj["attribute"], '');
        }else if(obj["displayType"] == 'text'){
            return Html.textHtml(obj["displayName"], obj["attribute"], '', required);
        }else if(obj["displayType"] == 'textarea'){
            return Html.textAreaHtml(obj["displayName"], obj["attribute"], '', required);
        }else if(obj["displayType"] == 'checkbox'){
            return Html.checkHtml(obj["displayName"], obj["attribute"], '', required);
        }else if(obj["displayType"] == 'select_static'){
            return Html.select_staticHtnl(obj["displayName"], obj["attribute"],'', required, obj);
        }else if(obj["displayType"] == 'radio'){
            return Html.radioHtml(obj["displayName"], obj["attribute"],'', required, obj);
        }else if(obj["displayType"] == 'file'){
            return Html.fileHtml(obj["displayName"], obj["attribute"],'', required, obj);
        }else{
            return '';
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
let Page = {
    table : null,
    recId: 0,
    edit: false,
    dataTablescolumnData : [],
    dataTablescolumnDef  : [],
    prepareView: function (){
        Page.initDataTableAndView();
        Page.loadTableList();
    },
    initDataTableAndView: function(){
        if(pageMetaData["importRecords"] && pageMetaData["importRecords"] == true){
            $("#importButton").show();
        }else{
            $("#importButton").hide();
        }
        $("#modal-files-nav").hide();
        $("#model-details-card-body").empty();
        if(pageMetaData && pageMetaData["uiRules"]){
            pageMetaData["uiRules"].forEach( function (obj){
                // 1. Prepare DataTables View
                let columnRec = {}
                if(obj && obj["attribute"] && obj["displayInList"] && obj["displayInList"] == true){
                    columnRec["title"]          = obj["displayName"];
                    columnRec["field"]           = obj["attribute"];
                    if(obj["width"]){  columnRec["width"] = obj["width"];  }
                    Page.dataTablescolumnData.push(columnRec);
                }
                // 2. Prepare Modal View
                let htmlContent = Html.getHtml(obj);
                if(obj["displayType"] && htmlContent != ''){
                   $("#model-details-card-body").append(Html.getHtml(obj));
                }
                // 3. Prepare Filters View
                console.log(obj["attribute"] + " - " + obj["displayName"] + " - " + obj["filter_type"] + " - " + obj["filter_field"]);
                if(obj["filterField"] && obj["filterField"] == true && obj["filterType"]){
                   $("#filter-field").append("<option value='"+obj["attribute"]+"'>"+obj["displayName"]+"</option>");
                }
            }); //endForEach

            var actionsButton = function(cell, formatterParams, onRendered){
                var buttonsHtml = '';
                buttonsHtml += '<div class="btn-group">';
                buttonsHtml += '<button type="button" class="btn btn-default btn-flat"><i class="fa fa-eye"></i></button>';
                buttonsHtml += '<button type="button" class="btn btn-default btn-flat"><i class="fa fa-pencil"></i></button>';
                buttonsHtml += '<button type="button" class="btn btn-default btn-flat"><i class="fa fa-trash"></i></button>';
                buttonsHtml += '</div>';
                return buttonsHtml;
            };
            Page.dataTablescolumnData.push({
                title: 'Actions',
                width:150,
                headerHozAlign:"center",
                hozAlign:"center",
                formatter: function(cell, formatterParams){

                    //create edit button
                    var editBt = document.createElement("button");
                    editBt.type = "button";
                    editBt.innerHTML = "<i class='fa fa-pencil'></i>";
                    editBt.classList.add("btn");
                    editBt.addEventListener("click", function(){
                        Page.addOrEditRec(cell.getRow().getData()._id,true);
                    });

                    //create edit button
                    var deleteBt = document.createElement("button");
                    deleteBt.type = "button";
                    deleteBt.innerHTML = "<i class='fa fa-trash'></i>";
                    deleteBt.classList.add("btn");
                    deleteBt.addEventListener("click", function(){
                        Page.deleteRec(cell.getRow().getData()._id);
                    });

                    //add buttons to cell (just the edit and delete buttons to start with)
                    var buttonHolder = document.createElement("span");
                    buttonHolder.appendChild(editBt);
                    buttonHolder.appendChild(deleteBt);
                    return buttonHolder;
                },
                });
        }
        /*
        if(pageMetaData["childrensCollection"] && pageMetaData["childrensCollection"]!= '' && pageMetaData["childrensEdit"]){
            let colData   = { "title" : "Children", "data" : null, "render" : function(data, type, row){  return "<a href='"+appPath+"/"+pageMetaData["childrensCollection"]+"/list.html?pid="+data._id+"'><i class='fas fa-child'></i></a>";  } };
            Page.dataTablescolumnData.push(colData);
            let colDef = {
                    "targets": Page.dataTablescolumnData.length-1,
                    "className": "text-center",
                    "width": "80"
                };
            Page.dataTablescolumnDef.push(colDef);
        }
        if(pageMetaData["filesEdit"] && pageMetaData["filesEdit"] == true){
            let colData   = { "title" : "Files", "data" : null, "render" : function(data, type, row){  return "<a href='"+appPath+"/"+pageMetaData["filesCollection"]+"/list.html?pid="+data._id+"'><i class='fas fa-file'></i></a>";  } };
            Page.dataTablescolumnData.push(colData);
            let colDef = {
                "targets": Page.dataTablescolumnData.length-1,
                "className": "text-center",
                "width": "60"
            };
            Page.dataTablescolumnDef.push(colDef);
        }

        if(pageMetaData["recordsEdit"] && pageMetaData["recordsEdit"] == true){
            let colData   = { "title" : "Edit", "data" : null, "render" : function(data, type, row){  return "<a href=javascript:Page.addOrEditRec('"+data._id+"',true)><i class='fas fa-edit'></i></a>";  } };
            Page.dataTablescolumnData.push(colData);
            let colDef = {
                "targets": Page.dataTablescolumnData.length-1,
                "className": "text-center",
                "width": "60"
            };
            Page.dataTablescolumnDef.push(colDef);
        }

        if(pageMetaData["recordsDelete"] && pageMetaData["recordsDelete"] == true){
            let colData = { "title" : "Delete", "data" : null, "render" : function(data, type, row){  return "<a href=javascript:Page.deleteRec('"+data._id+"')><i class='fas fa-trash'></i></a>";  } };
            Page.dataTablescolumnData.push(colData);
            let colDef = {
                "targets": Page.dataTablescolumnData.length-1,
                "className": "text-center",
                "width": "70"
            };
            Page.dataTablescolumnDef.push(colDef);
        }
        */
    },
    loadTableList:function (){
        $("#mainOverlay").show();
        Page.table = new Tabulator("#tabulatorList", {
            layout:"fitColumns",
            placeholder:"No Data Set",
            pagination:true,
            paginationMode:"remote",
            ajaxURL: appPath +"/api/"+pageCollection,
            paginationSize:20,
            paginationSizeSelector:[20, 50, 100],
            movableColumns:true,
            paginationCounter:"rows",
            columns: Page.dataTablescolumnData,
            dataSendParams:{
                "page":"p",
                "size":"s",
            } ,
            dataReceiveParams:{
                "last_row": "records_total", //change last_row parameter name to "rows_total"
                "last_page":"pages_total", //change last_page parameter name to "max_pages"
            } ,
            ajaxResponse:function(url, params, response){
                $("#mainOverlay").hide();
                return response;
            },
            ajaxFiltering:true,
            filterMode:"remote",
        });

    },
    addOrEditRec:function (recId,edit){
        Page.resetForm("detailsModalForm");
        Page.recId = recId;
        Page.edit  = edit;
        var addorEditLabel = "";

        if(edit){
            addorEditLabel = " Edit Record In "+pageTitle;
            Page.loadRecordData();
            //$('#fileslistTbl').DataTable().draw();
        }else{
            addorEditLabel = " Add Record In "+pageTitle;
        }
        $("#thisModalTitle").text(addorEditLabel);
        $("#thisModal").modal("show");
    },
    loadRecordData: function(){
        $.ajax({
            type : 'GET',
            url : appPath + '/api/'+pageCollection+'/'+Page.recId,
            contentType: 'application/json',
            success: function(response){
                if (response?.data != null && response.data.length > 0){
                    Page.setDataForEditing(response.data[0]);
                }else{
                    Common.showError(response);
                }
            },
            error: function(error){
                Common.showError(error);
            }
        });
    },
    setDataForEditing:function (obj){
        $.each(obj, function(key,val){
            if(key == '_id'){
                $("#thisModalTitle").text("Editing Record ID:  "+val+"  In "+pageTitle);
            }else{
                if($("#mod_"+key).attr('type') && $("#mod_"+key).attr('type') == 'checkbox'){
                   $("#mod_"+key).prop('checked', true);
                }else if( $('input[name="mod_'+ key+'"]') && $('input[name="mod_'+ key+'"]').attr('type') == 'radio' ){
                    $('input[name="mod_'+ key+'"][value="'+val+'"]').prop('checked', true);
                }else {
                   $("#mod_" + key).val(val);
                }
            }
        });
    },
    saveRecord: function (){
        $(".fieldError").remove();
        var httpMethod = 'POST';
        var httpAPIURL = appPath + '/api/'+pageCollection;
        if(Page.edit){ httpMethod = 'PUT';  httpAPIURL = appPath + '/api/update/'+pageCollection+'/'+Page.recId;  }

        if(pageMetaData && pageMetaData["uiRules"]){
            var jsonData  = {}; let isValidSave = true;
            //console.log("uiRules "+pageMetaData["uiRules"]);
            pageMetaData["uiRules"].forEach( function (obj){
                var attrName  = obj["attribute"];
                // Skip if _id & new record
                if(attrName == '_id' && !Page.edit){ return; }

                if(obj["displayType"] == 'checkbox') {
                    let fieldValue = $("#mod_" + attrName).prop('checked');
                    if(obj["mandatory"] && fieldValue  != true ){
                        isValidSave = false;
                        $("#mod_"+attrName).after("<p style='padding-top: 10px;' class='fieldError'>Please enter/select the mandatory field!</p>");
                    }
                    jsonData[attrName] = fieldValue;
                }else if(obj["displayType"] == 'radio') {
                    var radioVal = $('input[name="mod_'+ attrName+'"]:checked').val();
                    if(obj["mandatory"] && (radioVal  == '' || radioVal == undefined) ){
                        isValidSave = false;
                        $('input[name="mod_'+ attrName+'"]:first').before("<p class='fieldError'>Please enter/select the mandatory field!</p>");
                    }
                    jsonData[attrName] = radioVal;
                }else{
                    let fieldValue = $.trim($("#mod_"+attrName).val());
                    if(obj["mandatory"] && fieldValue  == '' ){
                        isValidSave = false;
                        $("#mod_"+attrName).after("<p class='fieldError'>Please enter/select the mandatory field!</p>");
                    }else if(obj["mandatory"] && obj["minLength"] || obj["maxLength"]){
                        let errorMessage = '';
                        if(fieldValue.length < obj["minLength"]) {isValidSave = false; errorMessage += 'Minimum '+obj["minLength"] + ' characters is mandatory'; }
                        if(fieldValue.length > obj["maxLength"]) {isValidSave = false; errorMessage += '<br/> Exceeded maximum allowed '+obj["minLength"] + ' characters!'; }
                        $("#mod_"+attrName).after("<p class='fieldError'>"+errorMessage+"</p>");
                    }
                    jsonData[attrName] = fieldValue;
                }
            });

            if(isValidSave){ $.ajax({
                type : httpMethod,
                url : httpAPIURL,
                data : JSON.stringify(jsonData),
                contentType: 'application/json',
                success: function(response){
                    $(".fieldError").remove();
                    if (response?.data != null && response.data.length > 0){
                        if(!Page.edit) { Page.resetForm("detailsModalForm");}
                        Common.showSuccess(response);
                    }else{
                        Common.showError(response);
                    }
                },
                error: function(error){
                    $(".fieldError").remove();
                    Common.showError(error);
                }
            }); }
        }
    },
    resetForm: function (formID){
        $(".fieldError").remove();
        $('#'+formID).each(function(){
            this.reset();
        });
    },
    refreshList: function(){
        Page.table.replaceData();
    },
    deleteRec: function (docID){
        if(confirm('This operation is irreversible! Are you sure you want to delete?')){
            $.ajax({
                type : 'DELETE',
                url : appPath + '/api/'+pageCollection+"/"+docID,
                contentType: 'application/json',
                success: function(response){
                    if (response?.data != null && response.data.length > 0){
                        Common.showSuccess(response);
                        Page.refreshList();
                    }else{
                        Common.showError(response);
                    }
                },
                error: function(error){
                    Common.showError(error);
                }
            });
        }
    },
    filterRecords: function(){
        var filterField = $("#filter-field").val();
        var filterValue = $("#filter-value").val();
        var filterType  = $("#filter-type").val();
        Page.table.setFilter(filterField, filterType, filterValue);
    },
    clearFilters: function(){
        Page.table.clearFilter();
        $("#filter-field").val('');
        $("#filter-type").val('');
        $("#filter-value").val('');
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
let Import = {
    success: 0,
    errors: 0,
    total: 0,
    finished: 0,
    percentage: 0,
    batchSize : 500,
    batches : [],
    runningBatch : 0,
    sheetData : null,
    upload:function (){
        $("#importModalTitle").text("Upload records to "+pageTitle);
        $("#importModal").modal("show");
    },
    startUploadFile:function (){
        $('.progress-bar').css('width', '0%').attr('aria-valuenow', 0);
        $("#errorMsgImport").hide();
        $("#mainImportTable").hide();
        $("#modalLoader2").show();
        Import.success = 0;
        Import.errors = 0,
        Import.finished     = 0;
        Import.percentage   = 0;
        Import.batches = [];
        Import.runningBatch = 0,
        $('.progress-bar').css('width', '0%').attr('aria-valuenow', 0);
        $("#importFileProgressBar").show();
        $("#importFileProgressBarMsg").text("Started Reading File");
        $("#importResponses").empty();
        event.preventDefault();
        var file = $('#importFile')[0].files[0]; // Get the first selected file
        if (file) {
            var reader = new FileReader();
            reader.onload = event => {
                var contents = Import.processExcel(event.target.result);
                Import.processRecords(contents);
            }
            reader.readAsBinaryString(file);
        } else {
            Common.showError("Failed to load file");
        }
    },
    //
    processExcel : function (data){
        var workbook = XLSX.read(data, {
            type: 'binary'
        });
        // var firstSheet = workbook.SheetNames[0];
        var data = Import.to_json_workbook(workbook);
        return data
    },
    //
    to_json_workbook: function (workbook) {
        var result = {};
            workbook.SheetNames.forEach(function(sheetName) {
            var roa = XLSX.utils.sheet_to_json(workbook.Sheets[sheetName], {
                header: 1
            });
            if (roa.length) result[sheetName] = roa;
        });
        return JSON.stringify(result, 2, 2);
    },
    processRecords:function (sheetData){
        Import.sheetData = JSON.parse(sheetData);
        Import.total = Import.sheetData["Sheet1"].length-1;
        Import.splitBatches();
        Import.makeAjaxCall();
    },
    splitBatches: function () {
        let multipleJsonData = [];
        $(Import.sheetData["Sheet1"]).each(function (index, record) {
            let jsonData = {};
            if(index > 0) {
                $(Import.sheetData["Sheet1"][0]).each(function (attIndex, attribute) {
                    jsonData[attribute] = Import.sheetData["Sheet1"][index][attIndex]; //  + (Math.random().toString());
                });
                multipleJsonData.push(jsonData);
                if(multipleJsonData.length >= Import.batchSize){
                    Import.batches.push(multipleJsonData);
                    multipleJsonData = [];
                }
            }
        });
        if(multipleJsonData.length > 0){
            Import.batches.push(multipleJsonData);
        }
    },
    makeAjaxCall: function (){
        var httpMethod = 'POST';
        var httpAPIURL = appPath + '/api/'+pageCollection+"/bulk";
        //var jsonData = {"documents" : Import.batches[Import.runningBatch]};
        var jsonData = Import.batches[Import.runningBatch];

            $.ajax({
                type : httpMethod,
                url : httpAPIURL,
                data : JSON.stringify(jsonData),
                contentType: 'application/json',
                success: function (response){
                    Import.showProgress(response);
                    if(Import.runningBatch < Import.batches.length){
                        Import.makeAjaxCall();
                    }
                },
                error: function (response){
                    Import.showProgress(response);
                    if(Import.runningBatch < Import.batches.length){
                        Import.makeAjaxCall();
                    }
                }
            });
    },
    showProgress:function (response){
        Import.finished += Import.batches[Import.runningBatch].length;
        Import.percentage = (Import.finished / Import.total) * 100;
        $('.progress-bar').css('width', Import.percentage+'%').attr('aria-valuenow', Import.percentage);
        $("#importFileProgressBarMsg").text("Processed "+ Import.finished + " out of "+ Import.total + " - " + Import.percentage.toFixed(2) + " % Completed");
        try{
            if(response?.status == 'error'){
                let jsonErrors = JSON.parse(response["errorDescription"]);
                $(jsonErrors).each(function (i,obj){
                    let rowId  = (((Import.runningBatch*Import.batchSize) + obj["index"]) +1);
                    let bodyRow = '<tr name="importResponsesTR" ><td style="font-size:12px;">'+ rowId + '</td>';
                        bodyRow += '<td style="font-size:12px;">' + obj["code"] + '</td>';
                        bodyRow += '<td style="font-size:12px;">' + obj["category"] + '</td>';
                        bodyRow += '<td style="font-size:12px;" colspan="2">' + obj["message"] + '</td>';
                    bodyRow += '</tr>';
                    $("#importResponses").append(bodyRow);
                    Import.errors++;
                });
            }
        }catch(e){ }

        console.log(Import.batches.length + " - Running " +Import.runningBatch);

        if( (Import.runningBatch+1) >= Import.batches.length) {
            $("#errorMsgImport").show();
            if(Import.errors > 0 ) {
                $("#mainImportTable").show();
                $("#errorMsgImport").html("<span style='color:red;'>" + Import.errors + " errors occurred! </span>");
            }else{
                $("#errorMsgImport").html("<span style='color:darkgreen'>Successfully completed without any errors!</span>");
            }
            $("#modalLoader2").hide();
        }
        Import.runningBatch++;
    },
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
let Files = {

}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
$(function () {
    if(pageParentID != '' && pageParentID != undefined && pageParentID != null){
        Common.setPageTitle(pageParentID + " - " +pageTitle);
    }else{
        Common.setPageTitle(pageTitle);
    }
    if(Common.pageError == false){
        Page.prepareView();
    }
    document.getElementById('thisModal').addEventListener('hidden.bs.modal', Page.refreshList);
    document.getElementById('importModal').addEventListener('hidden.bs.modal', Page.refreshList);
});
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////