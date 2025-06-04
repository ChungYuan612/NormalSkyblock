## AI + Minecraft

主要的程式碼放在src/main內，主程式是 **NormalSkyblock.java**

- Command 裡面負責處理玩家在遊戲內輸入指令/talk時可以打開對話模式、/test是管理員用來測試AI連線的指令
- Event: 裡面負責處理玩家對話的掌控，包含對村民右鍵開啟對話的模式，並且有掌控不同玩家對話的Session
- GeminiConnect: 主要的連接AI與處理村民相關事項
  - NPC資料夾主要定義NPC相關動作與轉換字串為村民可以執行的動作
  - Prompt資料夾內放與每個村民的對話控制和個性等
  - API主要把Client的方法放進來，可以在Command和Event用
  - 其他Gemini的開頭的類別都是在處理有關AI連線的
- NPC: 舊版的資料夾，移到裡面了
- Utils: 一些常用函式
