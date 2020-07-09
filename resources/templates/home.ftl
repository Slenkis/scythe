<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Home</title>

    <link href="/static/index.css" rel="stylesheet">
    <script src="/static/query.js" type="application/javascript"></script>
</head>
<body>
<div class="container">
    <div class="item counter">${model.minutes} мин</div>
    <input id="value-input" class="item value-input" type="number">
    <div class="item controls">
        <button class="send-btn" onclick="reset()">Сбросить</button>
        <button class="send-btn" onclick="add()">Добавить</button>
    </div>
</div>
</body>
</html>