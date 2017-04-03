
/**
 * Provides suggestions for state names (USA).
 * @class
 * @scope public
 */
function StateSuggestions() {
 
};

StateSuggestions.prototype.getSuggestions = function(textboxValue) {
    var oThis = this;
    var query = escape(textboxValue);
    var xmlRequest = new XMLHttpRequest();
    var url = "/eBay/suggest?q=" + query;
    var values = [];
    xmlRequest.onreadystatechange = function() {
        if(xmlRequest.readyState == 4 && xmlRequest.status == 200) {
            // alert(xmlRequest.responseText);
            var xmlData = xmlRequest.responseXML.documentElement.childNodes;
            // alert(xmlData.length);
            if(xmlData.length > 0) {
                for(var i = 0; i < xmlData.length; ++i) {
                    // alert(xmlData[i].childNodes[0].getAttribute("data"));
                    values.push(xmlData[i].childNodes[0].getAttribute("data"));
                }
                this.suggestions = values;
                // alert("in get suggestions " + this.suggestions.length);
                // oThis.showSuggestions(this.suggestions);
            }
            // else {
            //     oThis.hideSuggestions();
            // }
        }
    }
    xmlRequest.open("GET", url, true);
    xmlRequest.send();
};
/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
    var sTextboxValue = oAutoSuggestControl.textbox.value;
    if (sTextboxValue.length > 0){
        var query = escape(sTextboxValue);
        var xmlRequest = new XMLHttpRequest();
        var url = "/eBay/suggest?q=" + query;
        var values = [];
        var aSuggestions = [];
        // this.getSuggestions(sTextboxValue);
        xmlRequest.onreadystatechange = function() {
            if(xmlRequest.readyState == 4 && xmlRequest.status == 200) {
                // alert(xmlRequest.responseText);
                var xmlData = xmlRequest.responseXML.documentElement.childNodes;
                // alert(xmlData.length);
                if(xmlData.length > 0) {
                    for(var i = 0; i < xmlData.length; ++i) {
                        // alert(xmlData[i].childNodes[0].getAttribute("data"));
                        values.push(xmlData[i].childNodes[0].getAttribute("data"));
                    }
                    // this.suggestions = values;
                    // alert("in get suggestions " + this.suggestions.length);
                    // oThis.showSuggestions(this.suggestions);
                    for (var i=0; i < values.length; i++) { 
                        if (values[i].indexOf(sTextboxValue) == 0) {
                            aSuggestions.push(values[i]);
                        } 
                    }
                    //provide suggestions to the control
                    oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
                }
                // else {
                //     oThis.hideSuggestions();
                // }
            }
    }
    xmlRequest.open("GET", url, true);
    xmlRequest.send();
        //search for matching states
        // alert("in requestSuggestions " + this.suggestions.length);
    }   
};