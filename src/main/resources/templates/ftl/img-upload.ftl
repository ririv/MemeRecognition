<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>EmojiRecognition</title>
    <link href="../../static/css/main.css" rel="stylesheet">
    <link href="../../static/css/lib/bootstrap.css" rel="stylesheet">
    <link href="../../static/css/lib/bootstrap-theme.css" rel="stylesheet">
    <script src="../../static/js/main.js"></script>
</head>
<body style="padding: 10px">
<div>
    <form method="POST" enctype="multipart/form-data" action="/fileUpload">
        <span class="btn btn-success fileinput-button">
            <span>选择图片</span>
            <input type="file" accept="image/*" name="filename">
        </span>
        <input type="submit" value="上传">
    </form>
</div>
<div>
    <#--判断是否上传文件-->
    <#if msg??>
        <span>${msg}</span><br>
    <#else >
        <span>${msg!("请上传图片")}</span><br>
    </#if>
    <#--显示图片，一定要在img中的src发请求给controller，否则直接跳转是乱码-->
    <#if filename??>
        <img alt="上传的图片" src="/show?filename=${filename}" style="width: 200px"/>
    </#if>
</div>

</body>
</html>