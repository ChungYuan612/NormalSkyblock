package me.cyperion.normalskyblock.GeminiConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeminiResponseParser {

    private static final Gson gson = new Gson();

    // 外部呼叫的方法：輸入 JSON 字串，回傳自訂包裝類
    public static GeminiResponse parse(String json) {
        return gson.fromJson(json, GeminiResponse.class);
    }

    // 主體類別：代表整份 Gemini API 回傳內容
    public static class GeminiResponse {
        @SerializedName("candidates")
        public List<Candidate> candidates;

        @SerializedName("usageMetadata")
        public UsageMetadata usageMetadata;

        @SerializedName("modelVersion")
        public String modelVersion;

        // 方便取得主要回答內容
        public String getFirstTextResponse() {
            if (candidates != null && !candidates.isEmpty()) {
                Candidate c = candidates.get(0);
                if (c.content != null && c.content.parts != null && !c.content.parts.isEmpty()) {
                    return c.content.parts.get(0).text;
                }
            }
            return null;
        }
    }

    // 子結構們：
    public static class Candidate {
        @SerializedName("content")
        public Content content;

        @SerializedName("finishReason")
        public String finishReason;

        @SerializedName("avgLogprobs")
        public Double avgLogprobs;
    }

    public static class Content {
        @SerializedName("parts")
        public List<Part> parts;

        @SerializedName("role")
        public String role;
    }

    public static class Part {
        @SerializedName("text")
        public String text;
    }

    public static class UsageMetadata {
        @SerializedName("promptTokenCount")
        public int promptTokenCount;

        @SerializedName("candidatesTokenCount")
        public int candidatesTokenCount;

        @SerializedName("totalTokenCount")
        public int totalTokenCount;

        @SerializedName("promptTokensDetails")
        public List<TokenDetail> promptTokensDetails;

        @SerializedName("candidatesTokensDetails")
        public List<TokenDetail> candidatesTokensDetails;
    }

    public static class TokenDetail {
        @SerializedName("modality")
        public String modality;

        @SerializedName("tokenCount")
        public int tokenCount;
    }
}

