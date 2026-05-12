# FixTooManyRegisteredChannels

一个 Minecraft Fabric 模组，用于解决 "Too Many Registered Channels" 错误。

## 问题描述

当使用包含大量模组的整合包进入原版/插件服务器时，可能会遇到以下错误：

```
Client disconnected with reason: You have too many plugin channels registered! Try removing some of your mods.
Client disconnected with reason: IllegalStateException : Too many registered channels @ com.google.common.base.Preconditions:513
```

这是因为 Minecraft 和 Fabric API 对注册的自定义网络频道数量有限制。当多个模组都注册自己的通信频道时，很容易超过这个限制。

## 解决方案

本模组通过 Mixin 技术在两个层面拦截频道注册：

### 1. Fabric API 层面
- **目标类**: `GlobalReceiverRegistry`
- **拦截方法**: `registerGlobalReceiver`
- **作用**: 阻止 Fabric API 层面的频道注册

### 2. Minecraft 原生层面
- **目标类**: `ClientPacketListener`
- **拦截方法**: `handleCustomPayload`
- **作用**: 阻止 Minecraft 原生客户端处理过多的自定义 payload

### ⚡ 智能检测
**重要特性**: 模组只在连接到多人游戏服务器时才拦截频道注册，在单人游戏、局域网世界或其他非多人游戏环境下不会进行任何拦截，确保不影响单人游戏体验。

## 使用方法

1. 安装所需的依赖：
   - Fabric API
   - YACL (Yet Another Config Lib)
   - Mod Menu（可选，用于图形化配置界面）

2. 将模组放入 `.minecraft/mods` 文件夹

3. 启动游戏，在 Mod Menu 中找到 "FixTooManyRegisteredChannels"

4. 启用 "禁用模组通道" 选项

5. 连接到多人游戏服务器

## 工作原理

- **单人游戏/局域网世界**: 不进行任何拦截，所有模组功能正常运行
- **多人游戏服务器**: 自动激活保护机制，阻止过多的频道注册
- **智能切换**: 无需手动切换，模组会自动检测当前游戏环境

## 注意事项

- ⚠️ 启用此选项后，在多人游戏中某些依赖自定义网络通道的模组功能可能无法正常工作
- ✅ 在单人游戏中完全不受影响，所有模组功能正常
- 建议只在遇到 "Too many registered channels" 错误时才启用此功能
- 对于需要网络通信的模组（如多人游戏功能、数据同步等），可能需要单独配置白名单

## 技术细节

### Mixin 注入点

1. **ExampleClientMixin.java**
   ```java
   @Mixin(value = GlobalReceiverRegistry.class, remap = false)
   - 拦截: registerGlobalReceiver(Identifier, Object)
   - 条件: 仅在 isMultiplayer() 返回 true 时生效
   - 返回: false（表示注册失败）
   ```

2. **MinecraftChannelRegistrationMixin.java**
   ```java
   @Mixin(ClientPacketListener.class)
   - 拦截: handleCustomPayload(ClientboundCustomPayloadPacket)
   - 条件: 仅在 isMultiplayer() 返回 true 时生效
   - 操作: cancel()（取消处理）
   ```

### 环境检测逻辑

```java
private boolean isMultiplayer() {
    Minecraft client = Minecraft.getInstance();
    return client.getConnection() != null;
}
```

通过检查 `Minecraft.getInstance().getConnection()` 是否为 null 来判断：
- `null`: 单人游戏或未连接服务器
- `非 null`: 已连接到多人游戏服务器

### 配置文件

配置保存在 `.minecraft/config/fix_too_many_registered_channels.json`

```json
{
  "disableModChannels": true
}
```

## 构建

```bash
./gradlew build
```

构建产物位于 `build/libs/` 目录

## 许可证

CC0-1.0

## 作者

SheepYhangCN
