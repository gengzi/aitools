package com.gengzi.ui.save;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Service
@State(name = "state" , storages = @Storage("SdkSettingsPlugin.xml" ))
public final class MySettings implements PersistentStateComponent<MySettings> {

  private static MySettings instance;
  public Map<String, String> componentStates = new HashMap<>();

  public static MySettings getInstance() {
    return ApplicationManager.getApplication()
            .getService(MySettings.class);
  }


  @Nullable
  @Override
  public MySettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull MySettings state) {
    this.componentStates = state.componentStates;
  }
}