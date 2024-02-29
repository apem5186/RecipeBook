# RecipeBook

유튜브 재생목록을 가져와서 수정 및 관리할 수 있게 
#

### 개발인원
- 1명

### 일정
- 2024-02-04 ~ 2024-02-27

## 사용기술
#### Backend
- Springboot
- Java

#### DB
- h2
- JawsDB MySQL

#### Test
- Swagger

#### Deployment
- Heroku

#### Frontend
- Thymeleaf

#### Api
- YouTube Data API v3
- OpenAI GPT-3.5 Turbo

## 내용

<details>
  <summary><h4>메인페이지</h4></summary>

  > <image src="https://github.com/apem5186/RecipeBook/assets/81023500/c4f71613-3640-46ee-a18c-7cda162627c1"/>
  #### [관련 코드]
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/HomeController.java#L29
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/youtube/YoutubeController.java#L32
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L145
</details>

<details>
  <summary><h4>개별페이지</h4></summary>

  > <image src="https://github.com/apem5186/RecipeBook/assets/81023500/f20a730a-81c1-43ea-b2d7-9ffd5c445d4d"/>
  #### [관련 코드]
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/HomeController.java#L46
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L179
</details>

<details>
  <summary><h4>수정</h4></summary>

  > <image src="https://github.com/apem5186/RecipeBook/assets/81023500/c2b8acd0-968b-4cc5-9298-79d250bf5f9c"/>
  #### [관련 코드]
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/youtube/YoutubeController.java#L42
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L190
</details>

<details>
  <summary><h4>삭제</h4></summary>

  > <image src="https://github.com/apem5186/RecipeBook/assets/81023500/0783f457-5125-4248-a15e-22ba77d3d7ee"/>
  #### [관련 코드]
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/youtube/YoutubeController.java#L58
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L205

</details>

<details>
  <summary><h4>즐겨찾기</h4></summary>

  > <image src="https://github.com/apem5186/RecipeBook/assets/81023500/e30d2eaf-8fac-40eb-97dc-0fbd2b87203a"/>
  #### [관련 코드]
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/youtube/YoutubeController.java#L69
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L220
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L231
</details>

<details>
  <summary><h4>검색</h4></summary>

  #### [관련 코드]
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/youtube/YoutubeController.java#L76
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/youtube/YoutubeController.java#L91
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L246
</details>

<details>
  <summary><h4>초기화, 새로고침</h4></summary>
  - 초기화 : 현재 데이터베이스에 저장된 정보를 초기화 하고 유튜브 플레이리스트를 다시 가져옴
  - 새로고침 : 데이터베이스에 없는 재생목록을 가져옴

  > <image src="https://github.com/apem5186/RecipeBook/assets/81023500/03eeca8d-b699-4121-ab07-29b7db2fda5b"/>
  #### [관련 코드]
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/youtube/YoutubeController.java#L114
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/youtube/YoutubeController.java#L102
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L257
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L271
</details>

<details>
  <summary><h4>설명글 요약</h4></summary>
  - chatGPT를 이용해서 동영상 설명글을 요약함

  > <image src="https://github.com/apem5186/RecipeBook/assets/81023500/e984b157-4744-49c9-b710-1114b73cbf07"/>
  #### [관련 코드]
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/controller/youtube/YoutubeController.java#L126
  https://github.com/apem5186/RecipeBook/blob/910e8c0c16773c59febd1e32a3f51dd2d57e285a/src/main/java/com/recipe/recipebook/service/PlaylistService.java#L292

</details>
