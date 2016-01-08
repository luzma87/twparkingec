<!DOCTYPE html>
%{--<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a>             is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>--}%
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>
        <g:layoutTitle default="TW Parking EC"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <imp:favicon/>
    <imp:importFonts/>
    <imp:bootstrap theme="lumen"/>
    <imp:importJs/>
    <imp:importCss/>

    <imp:css href="${resource(dir: 'css', file: 'sticky-footer-navbar.css')}"/>

    <g:layoutHead/>
</head>

<body>
    <mn:navbar title="${g.layoutTitle(default: 'TW Parking EC')}"/>

    <!-- Begin page content -->
    <div class="container">
        <g:layoutBody/>
    </div>
</body>
</html>
