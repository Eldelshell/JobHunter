<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style type="text/css">
      <#include "styles.html">
    </style>
    
</head>
<body>
  <div class="container-fluid">
    <div class="row">
      <div class="col-lg-12">
        <h2 style="color: black;">${subscription.portal!""}</h2>
        
        <h3>
          <#if subscription.link??>
            <a href="${subscription.getWorkingLink()}">${subscription.position}</a>
          <#else>
            ${subscription.position!""}
          </#if>
        </h3>
        
      </div>
    </div>
      
    <hr>
      
    <div class="row">
      <div class="col-lg-12">
        <div style="white-space: pre-wrap;">${subscription.description}</div>
      </div>
    </div>
    
  </div>
</body>
</html