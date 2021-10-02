import requests
import smtplib
import datetime
import firebase_admin

from bs4 import BeautifulSoup
from firebase_admin import credentials
from firebase_admin import db
from urls import url, location, college_website_link

cred = credentials.Certificate(location)
firebase_admin.initialize_app(cred, {
    'databaseURL': url
})

ref = db.reference('notice')

now = datetime.datetime.now()
content = ''

noticeTextArr = []
noticeIdsArr = []

def extract_notice(url):
    content = ''
    
    res = requests.get(url)
    content = res.content
    # print(content)

    

    soup = BeautifulSoup(content, 'html.parser')
    for i, tag in enumerate(soup.find_all('script', attrs={'language': 'JavaScript1.2'})):
        if i == 1:
            content += format(tag.text).encode()
            content = str(content)

            startsArr = []
            for starts in range(len(content)):
                if content.startswith('<a class=login_err href=misc/news.asp?id=', starts):
                    startsArr.append(starts)
            # print(startsArr)

            for e in startsArr:
                if len(noticeIdsArr) < len(startsArr) / 2:
                    noticeIdsArr.append(str(content)[e+41:e+41+4])
            # print(str(ids))

            endsArr = []
            for ends in range(len(content)):
                if (content.startswith('</a><br><br>', ends)):
                    endsArr.append(ends)
            # print(endsArr)


            for idx in range(len(startsArr)):
                if idx < len(startsArr) / 2:
                    noticeTextArr.append(content[startsArr[idx]+48:endsArr[idx]])
            # print(noticeTextArr)

    return (noticeTextArr)


cnt = extract_notice(college_website_link)
print(cnt)
print(noticeIdsArr)

for i in range(len(cnt)):
    ref.child(noticeIdsArr[i]).set({
        'text': cnt[i],
        'link': noticeIdsArr[i]
    })
