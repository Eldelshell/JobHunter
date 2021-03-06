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
        <div class="col-xs-8">
          <h2>
            <#if job.link??>
                <a href="${job.link}">${job.position}</a>
            <#else>
                ${job.position}
            </#if>
          </h2>
          <h4>
            <#if job.company.url??>
                <a href="${job.company.url}">${job.company.name!""}</a>
            <#else>
                ${job.company.name!""}
            </#if>
          
            <#if job.address??>
                (${job.address})
            </#if>
          </h4>
          <#if job.portal??>
            <h4>${job.portal}</h4>
          </#if>
        </div>
        
        <div class="col-xs-4" style="text-align: right;">
          <h2>
            <#if job.rating == 0>
                &#9734; &#9734; &#9734; &#9734; &#9734;
            <#elseif job.rating == 1>
                &#9733; &#9734; &#9734; &#9734; &#9734;
            <#elseif job.rating == 2>
                &#9733; &#9733; &#9734; &#9734; &#9734;
            <#elseif job.rating == 3>
                &#9733; &#9733; &#9733; &#9734; &#9734;
            <#elseif job.rating == 4>
                &#9733; &#9733; &#9733; &#9733; &#9734;
            <#else>
                &#9733; &#9733; &#9733; &#9733; &#9733;
            </#if>
          </h2>
        </div>
      </div>
      
      <div class="row">
        <div class="col-lg-12">
            ${label_salary} ${job.salary!"N/A"}
        </div>
      </div>
      
      <#if (job.contacts?size > 0) >
        <div class="row">
          <div class="col-lg-12">
            <h2>${label_contacts}</h2>
            <table class="table table-condensed">
              <tbody style="font-size: 12px;">
                <#list job.contacts as contact>
                  <tr>
                    <td>${contact.name!""}</td>
                    <td>${contact.position!""}</td>
                    <td>
                      <#if contact.phone??>
                        <a href="tel:${contact.phone}">
                          &#9742; ${contact.phone}
                        </a>
                      </#if>
                    </td>
                    <td>
                      <#if contact.email??>
                        <a href="mailto:${contact.email}">
                          &#9993; ${contact.email}
                        </a>
                      </#if>
                    </td>
                  </tr>
                </#list>
              </tbody>
            </table>
          </div>
        </div>
      </#if>
      
      <div class="row">
        <div class="col-lg-12">
            <div style="white-space: pre-wrap;">${job.description}</div>
        </div>
      </div>
    </div>
</body>
</html>