package com.theah64.xrob.api.utils.clientpanel;

/**
 * Created by theapache64 on 14/9/16,7:28 AM.
 */
public class HtmlTemplates {

    public static class SearchTemplate {

        private final String tableName, trClass;

        public SearchTemplate(String tableName, String trClass) {
            this.tableName = tableName;
            this.trClass = trClass;
        }

        public String getSearchScript() {
            return String.format("var $rows = $('table#%s tr.%s'); $('input#iSearch').keyup(function () { var val = '^(?=.*\\\\b' + $.trim($(this).val()).split(/\\s+/).join('\\\\b)(?=.*\\\\b') + ').*$', reg = new RegExp(val, 'i'), text; $rows.show().filter(function () { text = $(this).text().replace(/\\s+/g, ' '); return !reg.test(text); }).hide(); });", tableName, trClass);
        }

        public String getTopTemplate(String h4Data, final String smallData) {
            return String.format("<div class=\"row\" style=\"margin-bottom: 20px;\"> <div class=\"col-md-8 pull-left\"> <h4>%s <small> %s </small> </h4> </div> <div class=\"col-md-4 pull-right\"> <div class=\"input-group\"> <span class=\"input-group-addon\"> <i class=\"glyphicon glyphicon-search\"></i> </span> <input id=\"iSearch\" placeholder=\"Search\" type=\"text\" class=\"form-control\"/> </div> </div> </div>", h4Data, smallData);
            //return String.format("<div class=\"row\" style=\"margin-bottom: 20px;\"> <div class=\"col-md-8 pull-left\"> <h4> %s </h4> </div> <div class=\"col-md-4 pull-right\"> <div class=\"input-group\"> <span class=\"input-group-addon\"> <i class=\"glyphicon glyphicon-search\"></i> </span> <input id=\"iSearch\" placeholder=\"Search\" type=\"text\" class=\"form-control\"/> </div> </div> </div>", topData);
        }
    }

    public static String getErrorHtml(String errorMessage) {
        return String.format("<div class=\"row\"> <h1 class=\"text-danger text-center\"><b>WTF!</b></br> %s </h1> </div>", errorMessage);
    }
}
