import time
import requests
from bs4 import BeautifulSoup
from pymongo import MongoClient
client = MongoClient()  # mongodb server
songs = client.kugou_db.songs # song collection
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36'
}
def get_info(url):
    '''获取酷狗音乐TOP500信息'''
    wb_data = requests.get(url, headers=headers)
    soup = BeautifulSoup(wb_data.text, 'lxml')
    ranks = soup.select('.pc_temp_num')  # 排名list
    # titles = soup.select('.pc_temp_songlist > ul > li > a')  # 名称list
    titles = soup.select('.pc_temp_songlist > ul > li')
    song_times = soup.select('.pc_temp_time')  # 歌曲时长list
    for rank, title, song_time in zip(ranks, titles, song_times):
        data = {
            'rank': rank.get_text().strip(),
            # 'singer': title.get_text().split('-')[0].strip(),
            # 'song': title.get_text().split('-')[1].strip(),
            'singer': title.get('title').split('-')[0].strip(),
            'song': title.get('title').split('-')[1].strip(),
            'time': song_time.get_text().strip()
        }
        print(data)
        song_id = songs.insert_one(data) # insert db
        print(song_id)
        print('---------------------------------')
if __name__ == '__main__':
    # 生成需要遍历的url
    urls = ['http://www.kugou.com/yy/rank/home/{}-8888.html'.format(str(i)) for i in range(1, 24)]
    for url in urls:
        get_info(url)
        # time.sleep(1)