// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MsgInfo.proto

package org.study.netty.domain;

public final class MsgInfo {
  private MsgInfo() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_org_study_netty_domain_MsgBody_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_org_study_netty_domain_MsgBody_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rMsgInfo.proto\022\026org.study.netty.domain\"" +
      "-\n\007MsgBody\022\021\n\tchannelId\030\001 \001(\t\022\017\n\007msgInfo" +
      "\030\002 \001(\tB#\n\026org.study.netty.domainB\007MsgInf" +
      "oP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_org_study_netty_domain_MsgBody_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_org_study_netty_domain_MsgBody_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_org_study_netty_domain_MsgBody_descriptor,
        new java.lang.String[] { "ChannelId", "MsgInfo", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
