package com.amphro.receptionmapper;

public enum NetworkType
{
  NTUKN(-1),
  NTNONE(0),
  NTEDGE(1),
  NT2G(3),
  NT3G(5),
  ;
  
  private int _networkStrength;
  
  private NetworkType(int networkStrength)
  {
    _networkStrength = networkStrength;
  }
  
  public int getNewtorkStrength()
  {
    return _networkStrength;
  }
}
