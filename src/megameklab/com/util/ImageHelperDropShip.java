/*
 * MegaMekLab - Copyright (C) 2010
 * 
 * Original author - jtighe (torren@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package megameklab.com.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import megamek.common.Aero;
import megamek.common.AmmoType;
import megamek.common.Bay;
import megamek.common.Dropship;
import megamek.common.Entity;
import megamek.common.EntityMovementMode;
import megamek.common.Mounted;
import megamek.common.weapons.BayWeapon;

public class ImageHelperDropShip {
    public static final String[] LOCATION_ABBRS =
        { "N", "LF", "RF", "A", "FL", "FR", "AL", "AR", "FL/FR", "AL/AR" };
    public static final int[] LOCATION_PRINT =
        { 0, 1, 2, 4, 5, 8, 9, 6, 7, 3 };
    public static final int LOC_FL = 4;
    public static final int LOC_FR = 5;
    public static final int LOC_AL = 6;
    public static final int LOC_AR = 7;
    public static final int LOC_FL_FR = 8;
    public static final int LOC_AL_AR = 9;

    public static void printDropShipNotes(Dropship dropship, Graphics2D g2d, int pointY) {

        int pointX = 22;
        boolean notesPrinted = false;
        double lineFeed = ImageHelper.getStringHeight(g2d, "H", g2d.getFont());

        Font font = UnitUtil.deriveFont(true, g2d.getFont().getSize2D());

        for (Mounted mount : dropship.getEquipment()) {
            if (mount.getLocation() == Entity.LOC_NONE) {
                if (!notesPrinted) {
                    g2d.setFont(font);
                    g2d.drawString("Notes: ", pointX, pointY);

                    pointY += lineFeed;

                    font = UnitUtil.deriveFont(g2d.getFont().getSize2D());
                    notesPrinted = true;
                    g2d.setFont(font);
                }
                g2d.drawString(mount.getName(), pointX, pointY);
                pointY += lineFeed;
            }
        }
    }

    public static void printDropShipCargo(Dropship dropship, Graphics2D g2d, int pointY) {

        if (dropship.getTransportBays().size() < 1) {
            return;
        }

        int pointX = 22;
        double lineFeed = ImageHelper.getStringHeight(g2d, "H", g2d.getFont());

        Font font = UnitUtil.deriveFont(true, g2d.getFont().getSize2D());

        g2d.setFont(font);
        g2d.drawString("Cargo: ", pointX, pointY);

        pointY += lineFeed;

        font = UnitUtil.deriveFont(g2d.getFont().getSize2D());

        g2d.setFont(font);
        for (Bay bay : dropship.getTransportBays()) {
            g2d.drawString(ImageHelperDropShip.getBayString(bay), pointX, pointY);
            pointY += lineFeed;
        }

    }

    public static void drawDropshipArmorPip(Graphics2D g2d, float width, float height) {
        ImageHelperDropShip.drawDropshipArmorPip(g2d, width, height, 9.0f);
    }

    public static void drawDropshipArmorPip(Graphics2D g2d, float width, float height, float fontsize) {
        Font font = new Font("Arial", Font.PLAIN, 6);
        font = font.deriveFont(fontsize);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);
        g2d.setBackground(Color.WHITE);
        g2d.drawString("O", width, height);
    }

    public static void drawDropshipISPip(Graphics2D g2d, int width, int height) {
        Dimension circle = new Dimension(7, 7);
        Dimension fillCircle = new Dimension(5, 5);
        g2d.setColor(Color.black);
        g2d.fillOval(width, height, circle.width, circle.height);
        g2d.setColor(Color.white);
        g2d.fillOval(width + 1, height + 1, fillCircle.width, fillCircle.height);
    }

    public static Font getDropShipWeaponsNEquipmentFont(Graphics2D g2d, boolean bold, float stringHeight, ArrayList<Hashtable<String, EquipmentInfo>> equipmentLocations, ArrayList<Hashtable<String, EquipmentInfo>> capitalEquipmentLocations, float pointSize) {

        Font font = g2d.getFont();
        boolean hasCapital = false;
        boolean hasSubCapital = false;

        int weaponCount = 1;
        for (int pos = Aero.LOC_NOSE; pos <= ImageHelperDropShip.LOC_AL_AR; pos++) {

            Hashtable<String, EquipmentInfo> eqHash = equipmentLocations.get(pos);
            if (eqHash.size() < 1) {
                continue;
            }

            hasSubCapital = true;

            for (EquipmentInfo eqi : eqHash.values()) {

                weaponCount++;
                if (eqi.isWeapon) {
                    if (eqi.isMML) {
                        weaponCount++;
                    } else if (eqi.isATM) {
                        weaponCount++;
                    } else if (eqi.hasArtemis || eqi.hasArtemisV) {
                        weaponCount++;
                    }
                    /*
                     * else { if (ImageHelper.getStringWidth(g2d,
                     * eqi.damage.trim(), font) > 22) { weaponCount++; } }
                     */

                    if (eqi.hasAmmo) {
                        weaponCount++;
                    }
                }
            }
        }

        for (int pos = Aero.LOC_NOSE; pos <= Aero.LOC_WINGS; pos++) {

            Hashtable<String, EquipmentInfo> eqHash = capitalEquipmentLocations.get(pos);
            if (eqHash.size() < 1) {
                continue;
            }

            hasCapital = true;

            for (EquipmentInfo eqi : eqHash.values()) {

                weaponCount++;
                if (eqi.isWeapon) {
                    if (eqi.isMML) {
                        weaponCount++;
                    } else if (eqi.isATM) {
                        weaponCount++;
                    } else if (eqi.hasArtemis || eqi.hasArtemisV) {
                        weaponCount++;
                    }
                    /*
                     * else { if (ImageHelper.getStringWidth(g2d,
                     * eqi.damage.trim(), font) > 22) { weaponCount++; } }
                     */

                    if (eqi.hasAmmo) {
                        weaponCount++;
                    }
                }
            }
        }

        if (hasCapital) {
            weaponCount += 2;
        }

        if (hasSubCapital) {
            weaponCount += 2;
        }

        while (((ImageHelper.getStringHeight(g2d, "H", font) * weaponCount) > stringHeight) && (pointSize > 0)) {
            pointSize -= .1;
            font = UnitUtil.deriveFont(bold, pointSize);
        }

        return font;
    }

    public static void printDropshipWeaponsNEquipment(Dropship dropship, Graphics2D g2d) {
        int qtyPoint = 26;
        int typePoint = 38;
        int locPoint = 111;
        int heatPoint = 135;
        int shtPoint = 151;
        int medPoint = 169;
        int longPoint = 192;
        int erPoint = 211;
        int nameSize = 68;
        float linePoint = 209f;
        float lineFeed = 6.7f;
        // float maxHeight = 260.9f;
        float stringHeight = 0f;
        float fontSize = 7.0f;
        boolean newLineNeeded = false;
        boolean hasCapital = false;
        boolean hasSubCapital = false;

        ArrayList<Vector<EquipmentInfo>> equipmentLocations = new ArrayList<Vector<EquipmentInfo>>(ImageHelperDropShip.LOCATION_ABBRS.length);
        ArrayList<Vector<EquipmentInfo>> capitalEquipmentLocations = new ArrayList<Vector<EquipmentInfo>>(ImageHelperDropShip.LOCATION_ABBRS.length);

        if (dropship.getMovementMode() == EntityMovementMode.AERODYNE) {
            linePoint = 201;
        }

        for (int pos = 0; pos < ImageHelperDropShip.LOCATION_ABBRS.length; pos++) {
            equipmentLocations.add(pos, new Vector<EquipmentInfo>());
            capitalEquipmentLocations.add(pos, new Vector<EquipmentInfo>());
        }

        for (Mounted eq : dropship.getWeaponBayList()) {

            if ((eq.isWeaponGroup() || (eq.getType() instanceof AmmoType)) || (eq.getLocation() == Entity.LOC_NONE) || !UnitUtil.isPrintableEquipment(eq.getType())) {
                continue;
            }

            Vector<EquipmentInfo> eqHash = equipmentLocations.get(eq.getLocation());
            Vector<EquipmentInfo> capitalEqHash = capitalEquipmentLocations.get(eq.getLocation());

            String equipmentName = "";
            if (eq.isRearMounted()) {
                switch (eq.getLocation()) {
                    case Aero.LOC_LWING:
                        eqHash = equipmentLocations.get(ImageHelperDropShip.LOC_AL);
                        capitalEqHash = capitalEquipmentLocations.get(ImageHelperDropShip.LOC_AL);
                        break;
                    case Aero.LOC_RWING:
                        eqHash = equipmentLocations.get(ImageHelperDropShip.LOC_AR);
                        capitalEqHash = capitalEquipmentLocations.get(ImageHelperDropShip.LOC_AR);
                        break;
                }
            }

            if ((eq.getType() instanceof BayWeapon) && ((BayWeapon) eq.getType()).isCapital()) {

                EquipmentInfo eqi = null;
                for (int weaponIndex : eq.getBayWeapons()) {
                    Mounted weapon = dropship.getEquipment(weaponIndex);

                    if ((eqi == null) || (equipmentName == "") || !equipmentName.equalsIgnoreCase(weapon.getName()) || (weapon.getType().getTechLevel() != eqi.techLevel)) {
                        if (eqi != null) {
                            eqi = new EquipmentInfo(dropship, weapon, eq);
                            equipmentName = eqi.name;
                            eqi.shouldIndent = true;
                            capitalEqHash.add(eqi);
                        } else {
                            eqi = new EquipmentInfo(dropship, weapon, eq);
                            capitalEqHash.add(eqi);
                            equipmentName = eqi.name;
                        }
                    } else {
                        eqi.count++;
                    }
                }
            } else {
                EquipmentInfo eqi = null;
                for (int weaponIndex : eq.getBayWeapons()) {
                    Mounted weapon = dropship.getEquipment(weaponIndex);

                    if ((eqi == null) || (equipmentName == "") || !equipmentName.equalsIgnoreCase(weapon.getName()) || (weapon.getType().getTechLevel() != eqi.techLevel)) {
                        if (eqi != null) {
                            eqi = new EquipmentInfo(dropship, weapon, eq);
                            eqi.shouldIndent = true;
                            eqHash.add(eqi);
                            equipmentName = eqi.name;
                        } else {
                            eqi = new EquipmentInfo(dropship, weapon, eq);
                            eqHash.add(eqi);
                            equipmentName = eqi.name;
                        }
                    } else {
                        eqi.count++;
                    }
                }
            }

        }

        equipmentLocations.get(ImageHelperDropShip.LOC_FL_FR).addAll(equipmentLocations.get(Aero.LOC_LWING));
        equipmentLocations.get(Aero.LOC_LWING).clear();
        equipmentLocations.get(Aero.LOC_RWING).clear();
        equipmentLocations.get(ImageHelperDropShip.LOC_AL_AR).addAll(equipmentLocations.get(ImageHelperDropShip.LOC_AL));
        equipmentLocations.get(ImageHelperDropShip.LOC_AL).clear();
        equipmentLocations.get(ImageHelperDropShip.LOC_AR).clear();

        capitalEquipmentLocations.get(ImageHelperDropShip.LOC_FL_FR).addAll(equipmentLocations.get(Aero.LOC_LWING));
        capitalEquipmentLocations.get(Aero.LOC_LWING).clear();
        capitalEquipmentLocations.get(Aero.LOC_RWING).clear();
        capitalEquipmentLocations.get(ImageHelperDropShip.LOC_AL_AR).addAll(equipmentLocations.get(ImageHelperDropShip.LOC_AL));
        capitalEquipmentLocations.get(ImageHelperDropShip.LOC_AL).clear();
        capitalEquipmentLocations.get(ImageHelperDropShip.LOC_AR).clear();

        Font font = UnitUtil.deriveFont(fontSize);
        g2d.setFont(font);

        // font = ImageHelper.getDropShipWeaponsNEquipmentFont(g2d, false,
        // maxHeight, equipmentLocations, capitalEquipmentLocations, fontSize);
        fontSize = font.getSize2D();
        g2d.setFont(font);
        stringHeight = ImageHelper.getStringHeight(g2d, "H", font);

        // linePoint -= stringHeight / 2;

        lineFeed = stringHeight;

        for (int pos = 0; pos < LOCATION_PRINT.length; pos++) {

            Vector<EquipmentInfo> eqHash = capitalEquipmentLocations.get(LOCATION_PRINT[pos]);

            if (eqHash.isEmpty()) {
                continue;
            }

            if (!hasCapital) {
                hasCapital = true;

                g2d.drawString("Capital Scale", typePoint, linePoint);
                font = UnitUtil.getNewFont(g2d, "(1-12) (13-24) (25-40) (41-50)", true, 75, fontSize);
                g2d.setFont(font);
                g2d.drawString("(1-12) (13-24) (25-40) (41-50)", shtPoint, linePoint);
                linePoint += lineFeed;

                font = UnitUtil.getNewFont(g2d, "Bay", true, 68, fontSize);
                g2d.setFont(font);

                g2d.drawString("Bay", typePoint, linePoint);
                g2d.drawString("Loc", locPoint, linePoint);
                g2d.drawString("Ht", heatPoint, linePoint);
                g2d.drawString("SRV", shtPoint, linePoint);
                g2d.drawString("MRV", medPoint, linePoint);
                g2d.drawString("LRV", longPoint, linePoint);
                g2d.drawString("ERV", erPoint, linePoint);
                linePoint += lineFeed;

                font = UnitUtil.deriveFont(fontSize);
                g2d.setFont(font);
            }

            for (EquipmentInfo eqi : eqHash) {
                newLineNeeded = false;

                if (eqi.shouldIndent) {
                    qtyPoint += 5;
                    typePoint += 5;
                    nameSize -= 10;
                }
                g2d.drawString(Integer.toString(eqi.count), qtyPoint, linePoint);
                String name = eqi.name.trim();

                font = UnitUtil.getNewFont(g2d, name, false, nameSize, fontSize);
                g2d.setFont(font);

                if (eqi.c3Level == EquipmentInfo.C3I) {
                    ImageHelper.printC3iName(g2d, typePoint, linePoint, font, false);
                } else if (eqi.c3Level == EquipmentInfo.C3S) {
                    ImageHelper.printC3sName(g2d, typePoint, linePoint, font, false);
                } else if (eqi.c3Level == EquipmentInfo.C3M) {
                    ImageHelper.printC3mName(g2d, typePoint, linePoint, font, false);
                } else if (eqi.c3Level == EquipmentInfo.C3SB) {
                    ImageHelper.printC3sbName(g2d, typePoint, linePoint, font, false);
                } else if (eqi.c3Level == EquipmentInfo.C3MB) {
                    ImageHelper.printC3mbName(g2d, typePoint, linePoint, font, false);
                } else {
                    g2d.drawString(name, typePoint, linePoint);
                    if (eqi.damage.trim().length() > 0) {
                        g2d.drawString(eqi.damage, typePoint, linePoint + lineFeed);
                        newLineNeeded = true;
                    }
                }
                if (eqi.shouldIndent) {
                    qtyPoint -= 5;
                    typePoint -= 5;
                    nameSize += 10;
                }
                font = UnitUtil.deriveFont(fontSize);
                g2d.setFont(font);

                String location = ImageHelperDropShip.LOCATION_ABBRS[LOCATION_PRINT[pos]];

                ImageHelper.printCenterString(g2d, location, font, locPoint + 5, linePoint);
                ImageHelper.printCenterString(g2d, Integer.toString(eqi.heat * eqi.count), font, heatPoint + 4, linePoint);
                if (eqi.shtRange > 0) {
                    g2d.drawString(String.format("%1$d", eqi.count * eqi.shtRange), shtPoint, (int) linePoint);
                } else {
                    g2d.drawLine(shtPoint, (int) linePoint - 2, shtPoint + 6, (int) linePoint - 2);
                }

                if (eqi.isAMS) {
                    g2d.drawString("Point Defense", medPoint, (int) linePoint);
                } else {
                    if (eqi.medRange > 0) {
                        g2d.drawString(String.format("%1$d", eqi.count * eqi.medRange), medPoint, (int) linePoint);
                    } else {
                        g2d.drawLine(medPoint, (int) linePoint - 2, medPoint + 6, (int) linePoint - 2);
                    }
                    if (eqi.longRange > 0) {
                        g2d.drawString(String.format("%1$d", eqi.count * eqi.longRange), longPoint, (int) linePoint);
                    } else {
                        g2d.drawLine(longPoint, (int) linePoint - 2, longPoint + 6, (int) linePoint - 2);
                    }
                    if (eqi.erRange > 0) {
                        g2d.drawString(String.format("%1$d", eqi.count * eqi.erRange), erPoint, (int) linePoint);
                    } else {
                        g2d.drawLine(erPoint, (int) linePoint - 2, erPoint + 6, (int) linePoint - 2);
                    }

                    float drawLine = linePoint + lineFeed;
                    if (newLineNeeded) {
                        drawLine += lineFeed;
                        linePoint += lineFeed;
                    }
                    if (eqi.hasArtemis) {
                        g2d.drawString("w/Artemis IV FCS", typePoint, drawLine);
                        newLineNeeded = true;
                    } else if (eqi.hasArtemisV) {
                        g2d.drawString("w/Artemis V FCS", typePoint, drawLine);
                        newLineNeeded = true;
                    } else if (eqi.hasApollo) {
                        g2d.drawString("w/Apollo FCS", typePoint, drawLine);
                        newLineNeeded = true;
                    }
                }
                linePoint += lineFeed;
                if (newLineNeeded) {
                    linePoint += lineFeed;
                }
            }
        }

        for (int pos = 0; pos < LOCATION_PRINT.length; pos++) {

            Vector<EquipmentInfo> eqHash = equipmentLocations.get(LOCATION_PRINT[pos]);

            if (eqHash.isEmpty()) {
                continue;
            }

            if (!hasSubCapital) {
                hasSubCapital = true;

                g2d.drawString("Standard Scale", typePoint, linePoint);
                font = UnitUtil.getNewFont(g2d, "(1-6) (7-12) (13-20) (21-25)", true, 75, fontSize);
                g2d.setFont(font);
                g2d.drawString("(1-6) (7-12) (13-20) (21-25)", shtPoint, linePoint);
                linePoint += lineFeed;

                font = UnitUtil.getNewFont(g2d, "Bay", true, 68, fontSize);
                g2d.setFont(font);

                g2d.drawString("Bay", typePoint, linePoint);
                g2d.drawString("Loc", locPoint, linePoint);
                g2d.drawString("Ht", heatPoint, linePoint);
                g2d.drawString("SRV", shtPoint, linePoint);
                g2d.drawString("MRV", medPoint, linePoint);
                g2d.drawString("LRV", longPoint, linePoint);
                g2d.drawString("ERV", erPoint, linePoint);
                linePoint += lineFeed;

                font = UnitUtil.deriveFont(fontSize);
                g2d.setFont(font);
            }

            for (EquipmentInfo eqi : eqHash) {
                newLineNeeded = false;

                if (eqi.shouldIndent) {
                    qtyPoint += 5;
                    typePoint += 5;
                    nameSize -= 10;
                }
                g2d.drawString(Integer.toString(eqi.count), qtyPoint, linePoint);
                String name = eqi.name.trim();

                font = UnitUtil.getNewFont(g2d, name, false, 68, fontSize);
                g2d.setFont(font);

                if (eqi.c3Level == EquipmentInfo.C3I) {
                    ImageHelper.printC3iName(g2d, typePoint, linePoint, font, false);
                } else if (eqi.c3Level == EquipmentInfo.C3S) {
                    ImageHelper.printC3sName(g2d, typePoint, linePoint, font, false);
                } else if (eqi.c3Level == EquipmentInfo.C3M) {
                    ImageHelper.printC3mName(g2d, typePoint, linePoint, font, false);
                } else if (eqi.c3Level == EquipmentInfo.C3SB) {
                    ImageHelper.printC3sbName(g2d, typePoint, linePoint, font, false);
                } else if (eqi.c3Level == EquipmentInfo.C3MB) {
                    ImageHelper.printC3mbName(g2d, typePoint, linePoint, font, false);
                } else {
                    g2d.drawString(name, typePoint, linePoint);
                    if (eqi.damage.trim().length() > 0) {
                        g2d.drawString(eqi.damage, typePoint, linePoint + lineFeed);
                        newLineNeeded = true;
                    }
                }
                if (eqi.shouldIndent) {
                    qtyPoint -= 5;
                    typePoint -= 5;
                    nameSize += 10;
                }
                font = UnitUtil.deriveFont(fontSize);
                g2d.setFont(font);

                String location = ImageHelperDropShip.LOCATION_ABBRS[LOCATION_PRINT[pos]];

                ImageHelper.printCenterString(g2d, location, font, locPoint + 5, linePoint);
                ImageHelper.printCenterString(g2d, Integer.toString(eqi.heat * eqi.count), font, heatPoint + 4, linePoint);
                if (eqi.shtRange > 0) {
                    String damage = String.format("%1$d (%2$d)", Math.round((eqi.count * eqi.shtRange) / 10), eqi.count * eqi.shtRange);
                    font = UnitUtil.getNewFont(g2d, damage, true, 17, fontSize);
                    g2d.setFont(font);
                    g2d.drawString(damage, shtPoint, (int) linePoint);
                    font = UnitUtil.deriveFont(fontSize);
                    g2d.setFont(font);
                } else {
                    g2d.drawLine(shtPoint, (int) linePoint - 2, shtPoint + 6, (int) linePoint - 2);
                }

                if (eqi.isAMS) {
                    g2d.drawString("Point Defense", medPoint, (int) linePoint);
                } else {
                    if (eqi.medRange > 0) {
                        String damage = String.format("%1$d (%2$d)", Math.round((eqi.count * eqi.medRange) / 10), eqi.count * eqi.medRange);
                        font = UnitUtil.getNewFont(g2d, damage, true, 17, fontSize);
                        g2d.setFont(font);
                        g2d.drawString(damage, medPoint, (int) linePoint);
                        font = UnitUtil.deriveFont(fontSize);
                        g2d.setFont(font);
                    } else {
                        g2d.drawLine(medPoint, (int) linePoint - 2, medPoint + 6, (int) linePoint - 2);
                    }
                    if (eqi.longRange > 0) {
                        String damage = String.format("%1$d (%2$d)", Math.round((eqi.count * eqi.longRange) / 10), eqi.count * eqi.longRange);
                        font = UnitUtil.getNewFont(g2d, damage, true, 17, fontSize);
                        g2d.setFont(font);
                        g2d.drawString(damage, longPoint, (int) linePoint);
                        font = UnitUtil.deriveFont(fontSize);
                        g2d.setFont(font);
                    } else {
                        g2d.drawLine(longPoint, (int) linePoint - 2, longPoint + 6, (int) linePoint - 2);
                    }
                    if (eqi.erRange > 0) {
                        String damage = String.format("%1$d (%2$d)", Math.round((eqi.count * eqi.erRange) / 10), eqi.count * eqi.erRange);
                        font = UnitUtil.getNewFont(g2d, damage, true, 17, fontSize);
                        g2d.setFont(font);
                        g2d.drawString(damage, erPoint, (int) linePoint);
                        font = UnitUtil.deriveFont(fontSize);
                        g2d.setFont(font);
                    } else {
                        g2d.drawLine(erPoint, (int) linePoint - 2, erPoint + 6, (int) linePoint - 2);
                    }

                    float drawLine = linePoint + lineFeed;
                    if (newLineNeeded) {
                        drawLine += lineFeed;
                        linePoint += lineFeed;
                    }
                    if (eqi.hasArtemis) {
                        g2d.drawString("w/Artemis IV FCS", typePoint, drawLine);
                        newLineNeeded = true;
                    } else if (eqi.hasArtemisV) {
                        g2d.drawString("w/Artemis V FCS", typePoint, drawLine);
                        newLineNeeded = true;
                    } else if (eqi.hasApollo) {
                        g2d.drawString("w/Apollo FCS", typePoint, drawLine);
                        newLineNeeded = true;
                    }
                }
                linePoint += lineFeed;
                if (newLineNeeded) {
                    linePoint += lineFeed;
                }
            }
        }

        linePoint += lineFeed;
        ImageHelperDropShip.printDropShipNotes(dropship, g2d, (int) linePoint);
        linePoint += lineFeed;
        ImageHelperDropShip.printDropShipCargo(dropship, g2d, (int) linePoint);

    }

    public static String getBayString(Bay bay) {
        StringBuffer returnString = new StringBuffer(bay.getUnusedString());

        if (bay.getDoors() > 0) {
            returnString.append("(");
            returnString.append(bay.getDoors());
            returnString.append(" doors)");
        }

        return returnString.toString();
    }
}