<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>SG MapleStore - Invoice List</title>
        
        <!-- Cascading Style Sheet (CSS) -->
        <link href="css/commoninfrastructure/baselayout/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="css/commoninfrastructure/baselayout/basetemplate.css" rel="stylesheet" type="text/css">
        <link href="css/commoninfrastructure/baselayout/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/commoninfrastructure/weblayout/CommonCSS.css" rel="stylesheet" type="text/css">
        
        <!-- Java Script (JS) -->
        <script src="js/commoninfrastructure/basejs/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/commoninfrastructure/basejs/jquery.min.js" type="text/javascript"></script>
        <script src="js/commoninfrastructure/basejs/metisMenu.min.js" type="text/javascript"></script>
        <script src="js/commoninfrastructure/basejs/jquery.newsTicker.js" type="text/javascript"></script>
        <script src="js/commoninfrastructure/webjs/CommonJS.js" type="text/javascript"></script>
    </head>
    <body onload="establishTime(); setInterval('updateTime()', 1000)">
        <div id="wrapper">
            <nav class="navbar navbar-default navbar-fixed-top" role="navigation" style="margin-bottom: 0;">
                <a class="navbar-brand" href="SGMapleStore?pageTransit=goToDashboard">
                    SG MapleStore
                </a>
                
                <!-- Top Navigation -->
                <div id="pageAnnouncement">
                    <div class="ccr-last-update">
                        <div class="update-ribon"><strong>Notification:</strong></div>
                        <span class="update-ribon-right"></span>
                        <div class="update-news-text">
                            <ul id="latestUpdate" class="newsticker">
                                <li><strong>System maintenance will be carried out at 15 Jan 2018, 00:00:00 (SGT).</strong></li>
                                <li><strong>Welcome to SG MapleStore!</strong></li>
                                <li><strong>Stay tune to our latest update via "Latest System Update".</strong></li>
                            </ul>
                        </div>
                        <div class="update-right-border">
                            <i class="fa fa-clock-o"></i>&nbsp;&nbsp;<strong><span id="clock"></span></strong>
                        </div>
                    </div>
                </div>
                <ul class="nav navbar-top-links navbar-right">
                    <li><a href="SGMapleStore?pageTransit=goToProfile"><i class="fa fa-user"></i>&nbsp;&nbsp;My Profile</a></li>
                    <li class="divider"></li>
                    <li><a href="SGMapleStore?pageTransit=goToLogout"><i class="fa fa-sign-out"></i>&nbsp;&nbsp;Logout</a></li>
                </ul>
            
                <!-- Left Navigation -->
                <div class="navbar-default sidebar">
                    <div class="sidebar-nav navbar-collapse">
                        <ul class="nav" id="side-menu">
                            <li>
                                <table border="0" style="margin: 12px 0px 12px 5px;" width="95%">
                                    <tr>
                                        <td rowspan="2" style="text-align: right;"><img src="images/ProfileImage.png" /></td>
                                        <td colspan="2" valign="middle" style="padding-left: 10px;"><strong>Hello <%= request.getAttribute("employeeNRIC")%>!</strong></td>
                                    </tr>
                                    <tr>
                                        <td style="padding-left: 10px;">
                                            <form action="SGMapleStore" method="POST">
                                                <input type="hidden" name="pageTransit" value="goToProfile"/>
                                                <button type="submit" class="btn btn-success btn-xs">My Profile</button>
                                            </form>
                                        </td>
                                        <td>
                                            <form action="SGMapleStore" method="POST">
                                                <input type="hidden" name="pageTransit" value="goToProfile"/>
                                                <button type="submit" class="btn btn-primary btn-xs">Contact Us</button>
                                            </form>
                                        </td>
                                    </tr>
                                </table>
                            </li>
                            <li><a href="SGMapleStore?pageTransit=goToDashboard"><i class="fa fa-home fa-fw"></i>&nbsp;&nbsp;Dashboard</a></li>
                            <li>
                                <a href="#"><i class="fa fa-users fa-fw"></i>&nbsp;&nbsp;Contacts<span class="fa arrow"></span></a>
                                <ul class="nav nav-second-level">
                                    <li><a href="SGMapleStore?pageTransit=goToContactList"><i class="fa fa-address-book fa-fw"></i>&nbsp;&nbsp;Contact List</a></li>
                                    <li><a href="SGMapleStore?pageTransit=goToNewContact"><i class="fa fa-user-plus fa-fw"></i>&nbsp;&nbsp;New Contact</a></li>
                                    <li><a href="SGMapleStore?pageTransit=goToNewEmployee"><i class="fa fa-user-plus fa-fw"></i>&nbsp;&nbsp;New Employee</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="#"><i class="fa fa-book fa-fw"></i>&nbsp;&nbsp;Inventory Items<span class="fa arrow"></span></a>
                                <ul class="nav nav-second-level">
                                    <li><a href="SGMapleStore?pageTransit=goToNewItemGroup"><i class="fa fa fa-cubes fa-fw"></i>&nbsp;&nbsp;Item Groups</a></li>
                                    <li><a href="SGMapleStore?pageTransit=goToNewItem"><i class="fa fa fa-cube fa-fw"></i>&nbsp;&nbsp;Items</a></li>
                                    <li><a href="SGMapleStore?pageTransit=goToQuantityAdjustment"><i class="fa fa fa-balance-scale fa-fw"></i>&nbsp;&nbsp;Quantity Adjustments</a></li>
                                    <li><a href="SGMapleStore?pageTransit=goToPriceAdjustment"><i class="fa fa fa-usd fa-fw"></i>&nbsp;&nbsp;Price Adjustments</a></li>
                                </ul>
                            </li>
                            <li>&nbsp;</li>
                            <li><a href="SGMapleStore?pageTransit=goToFirstHouse"><i class="fa fa-shopping-cart fa-fw"></i>&nbsp;&nbsp;Sales Orders</a></li>
                            <li><a href="SGMapleStore?pageTransit=goToFirstHouse"><i class="fa fa-cube fa-fw"></i>&nbsp;&nbsp;Packages</a></li>
                            <li><a href="SGMapleStore?pageTransit=goToInvoiceList"><i class="fa fa-file-text fa-fw"></i>&nbsp;&nbsp;Invoices</a></li>
                            <li><a href="SGMapleStore?pageTransit=goToFirstHouse"><i class="fa fa-shopping-bag fa-fw"></i>&nbsp;&nbsp;Purchase Orders</a></li>
                            <li><a href="SGMapleStore?pageTransit=goToFirstHouse"><i class="fa fa-list-alt fa-fw"></i>&nbsp;&nbsp;Bills</a></li>
                            <li>&nbsp;</li>
                            <li><a href="SGMapleStore?pageTransit=goToFirstHouse"><i class="fa fa-line-chart fa-fw"></i>&nbsp;&nbsp;Reports</a></li>
                        </ul>
                    </div>
                </div>
            </nav>

            <!-- Content Space -->
             <div id="page-wrapper">
                <div class="contentFill contentLayout">
                    <h3>Invoice List</h3>
                </div>
                <table class="table zi-table table-hover" id="contactList">
                    <thead>
                        <tr>
                            <th class="bulk-selection-cell"><input type="checkbox" /></th>
                            <th style="width: 20%;" class="sortable text-left">
                                <div class="placeholder-container">
                                    <div class="pull-left over-flow">Customer Name</div>
                                </div>
                            </th>
                            <th style="width: 20%;" class="sortable text-left">
                                <div class="placeholder-container">
                                    <div class="pull-left over-flow">Invoice Date</div>
                                </div>
                            </th>
                            <th style="width: 18%;" class="sortable text-left">
                                <div class="placeholder-container">
                                    <div class="pull-left over-flow">Invoice Number</div>
                                </div>
                            </th>
                            <th style="width: 18%;" class="sortable text-left">
                                <div class="placeholder-container">
                                    <div class="pull-left over-flow">Order Number</div>
                                </div>
                            </th>
                            <th style="width: 20%;" class="sortable text-left">
                                <div class="placeholder-container">
                                    <div class="pull-left over-flow">Total Amount</div>
                                </div>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            ArrayList<Vector> invoiceList = (ArrayList) request.getAttribute("invoiceList");
                            if(invoiceList.isEmpty()){
                        %>
                        <tr>
                            <td colspan="6" style="text-align: center;">There are no invoice records available.</td>
                        </tr>
                        <%
                            }
                            else {
                                for(int i = 0; i <= invoiceList.size()-1; i++){
                                    Vector v = invoiceList.get(i);
                                    String invoiceCustomer = String.valueOf(v.get(0));
                                    String invoiceDate = String.valueOf(v.get(1));
                                    String invoiceID = String.valueOf(v.get(2));
                                    String salesOrderID = String.valueOf(v.get(3));
                                    String totalAmount = String.valueOf(v.get(4));
                   
                        %>
                        <tr tabindex="-1" class="active">
                            <td class="bulk-selection-cell"><input type="checkbox" /></td>
                            <td><%= invoiceCustomer %></td>
                            <td><%= invoiceDate %></td>
                            <td><%= invoiceID %></td>
                            <td><%= salesOrderID %></td>
                            <td><%= "$ "+totalAmount %></td>
                            <%      }   %>
                            <%  }   %>
                        </tr>
                    </tbody>
                </table>
                <div id="modal-iframe"></div>
            </div>
        </div>
    </body>
</html>