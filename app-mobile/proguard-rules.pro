-keepnames class com.jycra.filmaico.data.user.dto.** { *; }
-keepclassmembers class com.jycra.filmaico.data.user.dto.** {
  <init>();
  <fields>;
}

-keepnames class com.jycra.filmaico.data.stream.dto.** { *; }
-keepclassmembers class com.jycra.filmaico.data.stream.dto.** {
  <init>();
  <fields>;
}

-keepnames class com.jycra.filmaico.data.movie.dto.** { *; }
-keepclassmembers class com.jycra.filmaico.data.movie.dto.** {
  <init>();
  <fields>;
}

-keepnames class com.jycra.filmaico.data.serie.dto.** { *; }
-keepclassmembers class com.jycra.filmaico.data.serie.dto.** {
  <init>();
  <fields>;
}

-keepnames class com.jycra.filmaico.data.channel.dto.** { *; }
-keepclassmembers class com.jycra.filmaico.data.channel.dto.** {
  <init>();
  <fields>;
}

-keepnames class com.jycra.filmaico.data.anime.dto.** { *; }
-keepclassmembers class com.jycra.filmaico.data.anime.dto.** {
  <init>();
  <fields>;
}

-keep class com.jycra.filmaico.domain.media.model.stream.Stream, com.jycra.filmaico.domain.stream.model.Stream** { *; }

# Es buena idea mantener también el DrmInfo si es una data class separada
-keep class com.jycra.filmaico.domain.media.model.stream.DrmInfo { *; }