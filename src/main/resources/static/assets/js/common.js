var Common = {
    pageError:false,
    //////////////////////////////////////////////////////////////////////////////////////////
    setPageTitle: function (title){
        $("#titleText").text(title);
        $("#bcText").text(title);
        $("#cardTitle").text(title);
        if(serverError == ''){
            Common.pageError = false;
            $("#serverError").hide();
        }else{
            Common.pageError = true;
            $("#serverError").show();
        }
    },
    //////////////////////////////////////////////////////////////////////////////////////////
    showSuccess: function (response) {
        console.log("response?.status. "+response?.status);
        if (response?.status.toLowerCase() === 'success' || response?.status.toLowerCase() === 'ok') {
            toastr.success(response.status.toUpperCase() + " - " + response?.description);
        } else {
            Common.showError(response);
        }
        try { $("#loader").hide();  $("#modalLoader").hide(); } catch (e) { }
    },
    //////////////////////////////////////////////////////////////////////////////////////////
    showError: function (response) {
        var responseObject = null;
        if(response?.responseText) {
            responseObject = JSON.parse(response.responseText);
        }else if(response?.responseJSON){
            responseObject = response.responseJSON;
        }else{
            responseObject = response;
        }
        if (responseObject?.status.toLowerCase() === 'error') {
            toastr.error(responseObject.status.toUpperCase() + " - " + responseObject?.description);
        } else {
            toastr.error(JSON.stringify(response));
        }
        try { $("#loader").hide(); $("#modalLoader").hide(); } catch (e) { }
    },
    //////////////////////////////////////////////////////////////////////////////////////////
    tableToCSV : function(elementName) {
    // Variable to store the final csv data
    let csv_data = [];

    // Get each row data
    let rows = document.getElementsByName(elementName);
    for (let i = 0; i < rows.length; i++) {

        // Get each column data
        let cols = rows[i].querySelectorAll('td,th');

        // Stores each csv row data
        let csvrow = [];
        for (let j = 0; j < cols.length; j++) {

            // Get the text data of each cell of
            // a row and push it to csvrow
            csvrow.push('\"' + cols[j].innerHTML + '\"');
        }

        // Combine each column value with comma
        csv_data.push(csvrow.join(","));
    }
    // Combine each row data with new line character
    csv_data = csv_data.join('\n');

    Common.downloadCSVFile(csv_data);

    /* We will use this function later to download
    the data in a csv file downloadCSVFile(csv_data);
    */
    },
    downloadCSVFile :function (csv_data) {

        // Create CSV file object and feed our
        // csv_data into it
        CSVFile = new Blob([csv_data], { type: "text/csv" });

        // Create to temporary link to initiate
        // download process
        let temp_link = document.createElement('a');

        // Download csv file
        temp_link.download = (Math.random() + 1).toString(36).substring(7) + ".csv";
        let url = window.URL.createObjectURL(CSVFile);
        temp_link.href = url;

        // This link should not be displayed
        temp_link.style.display = "none";
        document.body.appendChild(temp_link);

        // Automatically click the link to trigger download
        temp_link.click();
        document.body.removeChild(temp_link);
    }

}