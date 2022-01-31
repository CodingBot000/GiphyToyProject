# GiphyToyProject




Giphy Api를 활용한 Test Toy Project

Coding style 및 구조 잡는 것 정도만 확인할수 있는 수준정도의 가벼운 프로젝트입니다.


****************************************
* 일부러 Coroutine과 RxJava를 같이썼습니다.
LoadMore가  필요한 부분은 Coroutine + Paging 처리
LoadMore가  필요없는  부분은 RxJava 입니다.
****************************************



- Library :
MVVM, DataBinding
Retrofit, RxJava, Paging3
Coroutine,
LiveData, 
Room DataBase

- Extends :
abstract class BaseActivity,
abstract class BaseFragment,
abstract class BaseViewModel,
abstract class SingleUseCase

- Domain Layer : 
* API
UseCaseGet*

* LocalDB
UseCaseDb*

- Model Layer :
Repository,
DataSource

- DI :
Dagger Hilt

[Add description]
* DI :
DaggerHilt

* UseCase :
UseCase로 분리해서 관림사를 더 세분화. 코드 중복 감소
SingUseCase를 이용하여 한번에 하나씩만 실행 -> disposable처리 적용

* WorkManager
15분에 한번씩 API call을 해서 이전 데이터와 새로가져온 데이터가 다른지 검사해서
다를 경우 notification에 새로운 데이터가 나타났다고 알림을 보여줌.

* Service 
Const.kt에서 FOREGROUND 상수값을 true로 변경하면 WorkManager대산 Service 구동
백그라운드로 앱이 내려가면 백그라운드에서 10초에 한번씩 req를 보내서 이전 데이터와 변경사항이있나 체크
있으면 noti로 변경사항이 발생한걸 알려주고 req 전송 중단.

* 검색어처리
검색시 입력문자 변경에 따라 검색버튼 클리없이 실시간 체크해서 검색실행 (RxJava사용)

* CompositeDisposable
모든 RxJava 네트워크, DB 접근 이벤트를 UseCase 로 옮겼기때문에 기존 ViewModel에는 더이상  compositeDisposable이 없음.
SigleUseCase를 통해 제거 후 사용으로 바로바로 제거가 되어 메모리누수 리스크를 최소화 시킴.



StaggerdGridLayout
읽어온 이미지들의 사이즈가 제각각이라 StaggeredGrid에 로딩하는데 사이즈를 모두 연산하느라 크기가 마구 변하는 현상을 방지하기 위한 
API 요청으로 받아온 이미지 Width, Height로 먼저 ImageView의 사이즈를 고정하여 안정적 로딩하도록 하였습니다.



[![IMAGE ALT TEXT HERE](https://i9.ytimg.com/vi/ZPj4myoBoHw/mqdefault.jpg?sqp=CPCn348G&rs=AOn4CLBXZc7NGtMJuFuf-CbFUPcgG1IUGA)](https://youtu.be/ZPj4myoBoHw)

이미지를 누르시면 앱 동작 영상 확인가능하십니다



[전체 구조도 요약]
![Alt text](https://github.com/CodingBot000/Staggered_ViewPager_Tab/blob/main/GiphyTestApp_v1.jpg?raw=true)

