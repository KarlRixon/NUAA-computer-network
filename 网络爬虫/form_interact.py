import json
import math
import time
import pymongo
import requests
client = pymongo.MongoClient('localhost',27017)
mydb = client['mydb']
lagou = mydb['lagou']
headers = {
    'Accept': 'application/json, text/javascript, */*; q=0.01',
    'Accept-Encoding': 'gzip, deflate, br',
    'Accept-Language': 'zh-CN,zh;q=0.8',
    'Connection': 'keep-alive',
    'Content-Length': '26',
    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
    'Cookie': 'user_trace_token=20181204112323-6df9fdbf-f81d-4328-ae53-fd8df4d1b015; SL_GWPT_Show_Hide_tmp=1; SL_wptGlobTipTmp=1; JSESSIONID=ABAAABAABEEAAJA820A1AD075588061911650D121ACC7B5; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1543893829; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22167773e462936b-00b7486db23f01-9393265-2073600-167773e462a1dd%22%2C%22%24device_id%22%3A%22167773e462936b-00b7486db23f01-9393265-2073600-167773e462a1dd%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_referrer_host%22%3A%22%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%7D%7D; LGSID=20181204112349-042fbdf6-f774-11e8-89e2-525400f775ce; PRE_UTM=; PRE_HOST=; PRE_SITE=; PRE_LAND=https%3A%2F%2Fpassport.lagou.com%2Flogin%2Flogin.html%3Fts%3D1543893828599%26serviceId%3Dlagou%26service%3Dhttp%25253A%25252F%25252Fwww.lagou.com%25252Fjobs%25252F%26action%3Dlogin%26signature%3D41E03B5606589A61876B78A36B0FFB4E; LGUID=20181204112349-042fbff1-f774-11e8-89e2-525400f775ce; _putrc=6D0CECC2C14C62A7123F89F2B170EADC; login=true; unick=%E6%8B%89%E5%8B%BE%E7%94%A8%E6%88%B75932; _ga=GA1.2.360662032.1543893880; _gat=1; hasDeliver=0; gate_login_token=ca8542d671ee564f2d95ced0125b337da1a0a596c867d7f4e4253694e6d3253d; index_location_city=%E6%B1%9F%E8%8B%8F; TG-TRACK-CODE=index_navigation; X_HTTP_TOKEN=a62d43af3f2bbc33e9936fb6250de7d5; LGRID=20181204112649-6f5c8e9c-f774-11e8-8cb7-5254005c3644; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1543894009; SEARCH_ID=332291ea9c3949cbabfb5fbe5ee2f827',
    'Host': 'www.lagou.com',
    'Origin': 'https://www.lagou.com',
    'Referer': 'https://www.lagou.com/jobs/list_python?labelWords=&fromSearch=true&suginput=',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36',
    'X-Anit-Forge-Code': '0',
    'X-Anit-Forge-Token': 'None',
    'X-Requested-With': 'XMLHttpRequest'
}
def get_page(url,params):
    html = requests.post(url,data=params,headers=headers)
    json_data = json.loads(html.text)
    total_count = json_data['content']['positionResult']['totalCount']
    page_number = math.ceil(total_count/15) if math.ceil(total_count/15)<30 else 30
    get_info(url,page_number)
def get_info(url, page):
    for pn in range(1,page+1):
        params = {
            'first':'false',
            'pn':str(pn),
            'kd':'Python'
        }
        try:
            html = requests.post(url,data=params,headers=headers)
            json_data = json.loads(html.text)
            results = json_data['content']['positionResult']['result']
            for result in results:
                infos = {
                    'businessZones': result['businessZones'],
                    'city': result['city'],
                    'companyFullName': result['companyFullName'],
                    'companyLabelList': result['companyLabelList'],
                    'companySize': result['companySize'],
                    'district': result['district'],
                    'education': result['education'],
                    'explain': result['explain'],
                    'financeStage': result['financeStage'],
                    'firstType': result['firstType'],
                    'formatCreateTime': result['formatCreateTime'],
                    'gradeDescription': result['gradeDescription'],
                    'imState': result['imState'],
                    'industryField': result['industryField'],
                    'jobNature': result['jobNature'],
                    'positionAdvantage': result['positionAdvantage'],
                    'salary': result['salary'],
                    'secondType': result['secondType'],
                    'workYear': result['workYear']
                }
                print('------------------')
                print(infos)
                lagou.insert_one(infos)
            time.sleep(2)
        except requests.exceptions.ConnectionError:
            pass
if __name__ == "__main__":
    url = 'https://www.lagou.com/jobs/positionAjax.json'
    params = {
        'first':'true',
        'pn':'1',
        'kd':'python'
    }
    get_page(url,params)