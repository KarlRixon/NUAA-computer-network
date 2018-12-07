from bs4 import BeautifulSoup as soup
html='''<html>
<head>
    <title class='ceshi'>super 哈哈  star</title>
</head>
<body>
    天下第一帅
    <p class='sister'>

        是不是
    </p>
</body>
</html>'''
#用BeautifulSoup解析数据  python3 必须传入参数二'html.parser' 得到一个对象，接下来获取对象的相关属性
html=soup(html,'html.parser')
# 读取title内容
print(html.title)
# 读取title属性
attrs=html.title.attrs
print(attrs)
# 获取属性attrs['class'] ---->['ceshi'] 这是一个list 通过下标可以获取值
print(attrs['class'][0])
# 读取body
print(html.body)
# 读取数据还可以通过BeautifulSoup的select方法
# html.select()
#按标签名查找
soup.select('title')
soup.select('body')
# 按类名查找
soup.select('.sister')
# 按id名查找
# p标签中id为link的标签
soup.select('p #link')
#取标签里面的值
soup.p.string
#取标签里属性值  通过href获取
html['href']