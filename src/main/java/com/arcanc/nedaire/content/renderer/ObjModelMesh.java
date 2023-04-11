/**
 * @author ArcAnc
 * Created at: 08.04.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.renderer;

import com.google.common.base.Preconditions;
import org.apache.commons.compress.utils.Lists;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.List;

public class ObjModelMesh
{
    private final List<Vector3f> vertices = Lists.newArrayList();
    private final List<Vector2f> uvs = Lists.newArrayList();
    private final List<Vector3f> normals = Lists.newArrayList();
    private final List<Face> faces = Lists.newArrayList();
    public static ObjModelMesh newModel()
    {
        return new ObjModelMesh();
    }
    public ObjModelMesh addVertex(float x, float y, float z)
    {
        return addVertex(new Vector3f(x, y, z));
    }
    public ObjModelMesh addVertex(Vector3f vertex)
    {
        Preconditions.checkNotNull(vertex);
        vertices.add(vertex);
        return this;
    }

    public ObjModelMesh addUV(float u, float v)
    {
        return addUV(new Vector2f(u, v));
    }
    public ObjModelMesh addUV(Vector2f uv)
    {
        Preconditions.checkNotNull(uv);
        uvs.add(uv);
        return this;
    }

    public ObjModelMesh addNormal(float x, float y, float z)
    {
        return addNormal(new Vector3f(x, y, z));
    }
    public ObjModelMesh addNormal(Vector3f normal)
    {
        Preconditions.checkNotNull(normal);
        normals.add(normal);
        return this;
    }
    public ObjModelMesh addFaceInfo(Vector3f vertex, Vector2f uv, Vector3f normal)
    {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(uv);
        Preconditions.checkNotNull(normal);
        vertices.add(vertex);
        uvs.add(uv);
        normals.add(normal);
        return this;
    }

    /**
     * @param v00 - vertex x index
     * @param v01 - uv x index
     * @param v02 - normal x index
     * @param v10 - vertex y index
     * @param v11 - uv y index
     * @param v12 - normal y index
     * @param v20 - vertex z index
     * @param v21 - uv z index
     * @param v22 - normal z index
     * @return modelMesh
     */
    public ObjModelMesh newFace (int v00, int v01, int v02, int v10, int v11, int v12, int v20, int v21, int v22)
    {
        this.faces.add(new Face(v00, v01, v02, v10, v11, v12, v20, v21, v22));
        return this;
    }

    /**
     *
     * @param vertices - x, y, z vertex index
     * @param uvs - x, y, z uv index
     * @param normals - x, y, z normal index
     * @return model
     */
    public ObjModelMesh newFace(Vector3i vertices, Vector3i uvs, Vector3i normals)
    {
        return newFace(vertices.x(), uvs.x(), normals.x(), vertices.y(), uvs.y(), normals.y(), vertices.z(), uvs.z(), normals.z());
    }
    public record Face (int v00, int v01, int v02,
                         int v10, int v11, int v12,
                         int v20, int v21, int v22)
    {
        public Face(int v00, int v01, int v02, int v10, int v11, int v12, int v20, int v21, int v22)
        {
            this.v00 = v00 - 1;
            this.v01 = v01 - 1;
            this.v02 = v02 - 1;
            this.v10 = v10 - 1;
            this.v11 = v11 - 1;
            this.v12 = v12 - 1;
            this.v20 = v20 - 1;
            this.v21 = v21 - 1;
            this.v22 = v22 - 1;
        }
    }

    public List<Face> getFaces()
    {
        return faces;
    }

    public List<Vector3f> getVertices()
    {
        return vertices;
    }

    public List<Vector2f> getUvs()
    {
        return uvs;
    }

    public List<Vector3f> getNormals()
    {
        return normals;
    }

    private ObjModelMesh()
    {

    }
}
